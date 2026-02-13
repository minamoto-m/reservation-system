"use client"

import { useState, useMemo } from "react"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { ChevronLeft, ChevronRight } from "lucide-react"
import { doctors } from "@/lib/mock-data"
import type { Reservation } from "@/lib/types"

interface ReservationCalendarProps {
  reservations: Reservation[]
}

const WEEKDAYS = ["日", "月", "火", "水", "木", "金", "土"]

export function ReservationCalendar({ reservations }: ReservationCalendarProps) {
  const [currentDate, setCurrentDate] = useState(new Date())

  const { year, month, days } = useMemo(() => {
    const y = currentDate.getFullYear()
    const m = currentDate.getMonth()

    const firstDay = new Date(y, m, 1)
    const lastDay = new Date(y, m + 1, 0)
    const startPadding = firstDay.getDay()
    const totalDays = lastDay.getDate()

    const daysArray: (number | null)[] = []

    for (let i = 0; i < startPadding; i++) {
      daysArray.push(null)
    }

    for (let i = 1; i <= totalDays; i++) {
      daysArray.push(i)
    }

    return { year: y, month: m, days: daysArray }
  }, [currentDate])

  const reservationsByDate = useMemo(() => {
    const map = new Map<string, Reservation[]>()
    for (const res of reservations) {
      const existing = map.get(res.date) || []
      map.set(res.date, [...existing, res])
    }
    return map
  }, [reservations])

  const handlePrevMonth = () => {
    setCurrentDate(new Date(year, month - 1, 1))
  }

  const handleNextMonth = () => {
    setCurrentDate(new Date(year, month + 1, 1))
  }

  const formatDateKey = (day: number) => {
    const m = String(month + 1).padStart(2, "0")
    const d = String(day).padStart(2, "0")
    return `${year}-${m}-${d}`
  }

  const isToday = (day: number) => {
    const today = new Date()
    return (
      today.getFullYear() === year &&
      today.getMonth() === month &&
      today.getDate() === day
    )
  }

  const getDoctorName = (doctorId: string) => {
    const doctor = doctors.find((d) => d.id === doctorId)
    return doctor?.name.split(" ")[0] || ""
  }

  return (
    <div className="rounded-lg border border-border bg-card">
      <div className="flex items-center justify-between border-b border-border px-4 py-3">
        <Button variant="ghost" size="icon" onClick={handlePrevMonth}>
          <ChevronLeft className="h-5 w-5" />
          <span className="sr-only">前月</span>
        </Button>
        <h2 className="text-lg font-semibold text-card-foreground">
          {year}年 {month + 1}月
        </h2>
        <Button variant="ghost" size="icon" onClick={handleNextMonth}>
          <ChevronRight className="h-5 w-5" />
          <span className="sr-only">翌月</span>
        </Button>
      </div>

      <div className="grid grid-cols-7">
        {WEEKDAYS.map((day, index) => (
          <div
            key={day}
            className={`border-b border-border px-2 py-2 text-center text-sm font-medium ${
              index === 0
                ? "text-destructive"
                : index === 6
                  ? "text-primary"
                  : "text-muted-foreground"
            }`}
          >
            {day}
          </div>
        ))}
      </div>

      <div className="grid grid-cols-7">
        {days.map((day, index) => {
          if (day === null) {
            return (
              <div
                key={`empty-${index}`}
                className="min-h-24 border-b border-r border-border bg-muted/30"
              />
            )
          }

          const dateKey = formatDateKey(day)
          const dayReservations = reservationsByDate.get(dateKey) || []
          const dayOfWeek = (index % 7)
          const isWeekend = dayOfWeek === 0 || dayOfWeek === 6

          return (
            <div
              key={day}
              className={`min-h-24 border-b border-r border-border p-1 ${
                isToday(day) ? "bg-primary/5" : ""
              }`}
            >
              <div
                className={`mb-1 flex h-6 w-6 items-center justify-center rounded-full text-sm ${
                  isToday(day)
                    ? "bg-primary font-semibold text-primary-foreground"
                    : dayOfWeek === 0
                      ? "text-destructive"
                      : dayOfWeek === 6
                        ? "text-primary"
                        : "text-card-foreground"
                } ${isWeekend ? "font-medium" : ""}`}
              >
                {day}
              </div>
              <div className="flex flex-col gap-0.5">
                {dayReservations.slice(0, 3).map((res) => (
                  <div
                    key={res.id}
                    className={`truncate rounded px-1 py-0.5 text-xs ${
                      res.status === "confirmed"
                        ? "bg-accent/20 text-accent"
                        : res.status === "pending"
                          ? "bg-chart-4/20 text-chart-4"
                          : "bg-destructive/20 text-destructive"
                    }`}
                    title={`${res.time} ${res.patientName} - ${getDoctorName(res.doctorId)}`}
                  >
                    {res.time} {res.patientName.split(" ")[0]}
                  </div>
                ))}
                {dayReservations.length > 3 && (
                  <Badge
                    variant="secondary"
                    className="h-5 justify-center text-xs"
                  >
                    +{dayReservations.length - 3}件
                  </Badge>
                )}
              </div>
            </div>
          )
        })}
      </div>
    </div>
  )
}
