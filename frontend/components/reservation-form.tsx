"use client"

import React from "react"

import { useState, useMemo } from "react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Plus } from "lucide-react"
import { departments, doctors, timeSlots } from "@/lib/mock-data"
import type { Reservation } from "@/lib/types"

interface ReservationFormProps {
  onSubmit: (reservation: Omit<Reservation, "id" | "createdAt">) => void
}

export function ReservationForm({ onSubmit }: ReservationFormProps) {
  const [open, setOpen] = useState(false)
  const [selectedDepartment, setSelectedDepartment] = useState("")
  const [formData, setFormData] = useState({
    patientName: "",
    patientPhone: "",
    doctorId: "",
    date: "",
    time: "",
    notes: "",
  })

  const filteredDoctors = useMemo(() => {
    if (!selectedDepartment) return doctors
    return doctors.filter((doc) => doc.departmentId === selectedDepartment)
  }, [selectedDepartment])

  const handleDepartmentChange = (value: string) => {
    setSelectedDepartment(value)
    setFormData((prev) => ({ ...prev, doctorId: "" }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (
      !formData.patientName ||
      !formData.patientPhone ||
      !formData.doctorId ||
      !formData.date ||
      !formData.time
    ) {
      return
    }

    onSubmit({
      ...formData,
      departmentId: selectedDepartment,
      status: "pending",
    })

    setFormData({
      patientName: "",
      patientPhone: "",
      doctorId: "",
      date: "",
      time: "",
      notes: "",
    })
    setSelectedDepartment("")
    setOpen(false)
  }

  const today = new Date().toISOString().split("T")[0]

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="gap-2">
          <Plus className="h-4 w-4" />
          新規予約
        </Button>
      </DialogTrigger>
      <DialogContent className="max-h-[90vh] overflow-y-auto sm:max-w-md">
        <DialogHeader>
          <DialogTitle>新規予約の作成</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="mt-4 flex flex-col gap-4">
          <div className="flex flex-col gap-2">
            <Label htmlFor="patientName">患者名</Label>
            <Input
              id="patientName"
              placeholder="山田 太郎"
              value={formData.patientName}
              onChange={(e) =>
                setFormData((prev) => ({ ...prev, patientName: e.target.value }))
              }
              required
            />
          </div>

          <div className="flex flex-col gap-2">
            <Label htmlFor="patientPhone">電話番号</Label>
            <Input
              id="patientPhone"
              type="tel"
              placeholder="090-1234-5678"
              value={formData.patientPhone}
              onChange={(e) =>
                setFormData((prev) => ({ ...prev, patientPhone: e.target.value }))
              }
              required
            />
          </div>

          <div className="flex flex-col gap-2">
            <Label htmlFor="department">診療科</Label>
            <Select value={selectedDepartment} onValueChange={handleDepartmentChange}>
              <SelectTrigger id="department">
                <SelectValue placeholder="診療科を選択" />
              </SelectTrigger>
              <SelectContent>
                {departments.map((dept) => (
                  <SelectItem key={dept.id} value={dept.id}>
                    {dept.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="flex flex-col gap-2">
            <Label htmlFor="doctor">担当医</Label>
            <Select
              value={formData.doctorId}
              onValueChange={(value) =>
                setFormData((prev) => ({ ...prev, doctorId: value }))
              }
            >
              <SelectTrigger id="doctor">
                <SelectValue placeholder="担当医を選択" />
              </SelectTrigger>
              <SelectContent>
                {filteredDoctors.map((doc) => (
                  <SelectItem key={doc.id} value={doc.id}>
                    {doc.name} ({doc.specialization})
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div className="flex flex-col gap-2">
              <Label htmlFor="date">予約日</Label>
              <Input
                id="date"
                type="date"
                min={today}
                value={formData.date}
                onChange={(e) =>
                  setFormData((prev) => ({ ...prev, date: e.target.value }))
                }
                required
              />
            </div>

            <div className="flex flex-col gap-2">
              <Label htmlFor="time">予約時間</Label>
              <Select
                value={formData.time}
                onValueChange={(value) =>
                  setFormData((prev) => ({ ...prev, time: value }))
                }
              >
                <SelectTrigger id="time">
                  <SelectValue placeholder="時間" />
                </SelectTrigger>
                <SelectContent>
                  {timeSlots.map((slot) => (
                    <SelectItem key={slot} value={slot}>
                      {slot}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>

          <div className="flex flex-col gap-2">
            <Label htmlFor="notes">備考</Label>
            <Textarea
              id="notes"
              placeholder="症状やご要望など"
              value={formData.notes}
              onChange={(e) =>
                setFormData((prev) => ({ ...prev, notes: e.target.value }))
              }
              rows={3}
            />
          </div>

          <div className="mt-2 flex justify-end gap-2">
            <Button
              type="button"
              variant="outline"
              onClick={() => setOpen(false)}
            >
              キャンセル
            </Button>
            <Button type="submit">予約を作成</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}
