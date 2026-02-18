"use client"

import { useState, useMemo, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { departmentApi } from "@/lib/api/department"
import { doctorApi } from "@/lib/api/doctor"
import { timeSlotApi, type TimeSlotAvailable } from "@/lib/api/time-slot"
import { reservationApi, type ReservationResponse } from "@/lib/api/reservation"
import type { Department } from "@/types/department"
import type { Doctor } from "@/types/doctor"
import { Calendar, Clock, User, Phone, Stethoscope, CheckCircle2, Building2, ArrowLeft, Loader2 } from "lucide-react"
import Link from "next/link"

type BookingStep = "department" | "doctor" | "datetime" | "info" | "confirm" | "complete"

export default function BookingPage() {
  const [departments, setDepartments] = useState<Department[]>([])
  const [departmentsLoading, setDepartmentsLoading] = useState(true)
  const [departmentsError, setDepartmentsError] = useState<string | null>(null)

  const [doctors, setDoctors] = useState<Doctor[]>([])
  const [doctorsLoading, setDoctorsLoading] = useState(false)
  const [doctorsError, setDoctorsError] = useState<string | null>(null)

  const [step, setStep] = useState<BookingStep>("department")
  const [selectedDepartment, setSelectedDepartment] = useState("")
  const [selectedDoctor, setSelectedDoctor] = useState("")
  const [selectedDate, setSelectedDate] = useState("")
  const [selectedTimeSlot, setSelectedTimeSlot] = useState<TimeSlotAvailable | null>(null)

  const [availableTimeSlots, setAvailableTimeSlots] = useState<TimeSlotAvailable[]>([])
  const [timeSlotsLoading, setTimeSlotsLoading] = useState(false)
  const [timeSlotsError, setTimeSlotsError] = useState<string | null>(null)
  const [patientName, setPatientName] = useState("")
  const [patientPhone, setPatientPhone] = useState("")
  const [notes, setNotes] = useState("")
  const [submitLoading, setSubmitLoading] = useState(false)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [createdReservation, setCreatedReservation] = useState<ReservationResponse | null>(null)

  useEffect(() => {
    departmentApi
      .getAll()
      .then(setDepartments)
      .catch((err) => setDepartmentsError(err instanceof Error ? err.message : "読み込みに失敗しました"))
      .finally(() => setDepartmentsLoading(false))
  }, [])

  useEffect(() => {
    if (!selectedDepartment) {
      setDoctors([])
      setDoctorsError(null)
      return
    }
    setDoctorsLoading(true)
    setDoctorsError(null)
    doctorApi
      .getByDepartmentId(Number(selectedDepartment))
      .then(setDoctors)
      .catch((err) => setDoctorsError(err instanceof Error ? err.message : "医師の読み込みに失敗しました"))
      .finally(() => setDoctorsLoading(false))
  }, [selectedDepartment])

  useEffect(() => {
    if (!selectedDoctor || !selectedDate) {
      setAvailableTimeSlots([])
      setTimeSlotsError(null)
      setSelectedTimeSlot(null)
      return
    }
    setTimeSlotsLoading(true)
    setTimeSlotsError(null)
    timeSlotApi
      .getAvailable(Number(selectedDoctor), selectedDate)
      .then(setAvailableTimeSlots)
      .catch((err) => setTimeSlotsError(err instanceof Error ? err.message : "枠の取得に失敗しました"))
      .finally(() => setTimeSlotsLoading(false))
  }, [selectedDoctor, selectedDate])

  const selectedDepartmentData = departments.find((d) => String(d.id) === selectedDepartment)
  const selectedDoctorData = doctors.find((d) => String(d.id) === selectedDoctor)

  const today = new Date().toISOString().split("T")[0]
  const maxDate = new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString().split("T")[0]

  const handleSubmit = async () => {
    if (!selectedTimeSlot) return
    setSubmitLoading(true)
    setSubmitError(null)
    try {
      const result = await reservationApi.create({
        timeSlotId: selectedTimeSlot.timeSlotId,
        name: patientName,
        phoneNumber: patientPhone,
      })
      setCreatedReservation(result)
      setStep("complete")
    } catch (err) {
      setSubmitError(err instanceof Error ? err.message : "予約の作成に失敗しました")
    } finally {
      setSubmitLoading(false)
    }
  }

  const goBack = () => {
    const steps: BookingStep[] = ["department", "doctor", "datetime", "info", "confirm"]
    const currentIndex = steps.indexOf(step)
    if (currentIndex > 0) {
      setStep(steps[currentIndex - 1])
    }
  }

  const resetForm = () => {
    setStep("department")
    setSelectedDepartment("")
    setSelectedDoctor("")
    setSelectedDate("")
    setSelectedTimeSlot(null)
    setPatientName("")
    setPatientPhone("")
    setNotes("")
    setCreatedReservation(null)
  }

  const stepIndicator = () => {
    const steps = [
      { key: "department", label: "診療科" },
      { key: "doctor", label: "医師" },
      { key: "datetime", label: "日時" },
      { key: "info", label: "情報" },
      { key: "confirm", label: "確認" },
    ]
    const currentIndex = steps.findIndex((s) => s.key === step)

    if (step === "complete") return null

    return (
      <div className="flex items-center justify-center gap-2 mb-8">
        {steps.map((s, index) => (
          <div key={s.key} className="flex items-center">
            <div
              className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-medium transition-colors ${
                index <= currentIndex
                  ? "bg-primary text-primary-foreground"
                  : "bg-muted text-muted-foreground"
              }`}
            >
              {index + 1}
            </div>
            {index < steps.length - 1 && (
              <div
                className={`w-8 h-0.5 mx-1 ${
                  index < currentIndex ? "bg-primary" : "bg-muted"
                }`}
              />
            )}
          </div>
        ))}
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-background">
      {/* Header */}
      <header className="border-b bg-card">
        <div className="max-w-3xl mx-auto px-4 py-4">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-lg bg-primary flex items-center justify-center">
              <Stethoscope className="h-5 w-5 text-primary-foreground" />
            </div>
            <div>
              <h1 className="text-lg font-semibold text-foreground">メディカルクリニック</h1>
              <p className="text-sm text-muted-foreground">オンライン予約</p>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-3xl mx-auto px-4 py-8">
        {stepIndicator()}

        {/* Step 1: Select Department */}
        {step === "department" && (
          <Card>
            <CardHeader className="text-center">
              <CardTitle className="flex items-center justify-center gap-2">
                <Building2 className="h-5 w-5" />
                診療科を選択
              </CardTitle>
              <CardDescription>ご希望の診療科をお選びください</CardDescription>
            </CardHeader>
            <CardContent>
              {departmentsLoading ? (
                <div className="flex items-center justify-center py-12">
                  <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
                </div>
              ) : departmentsError ? (
                <p className="text-center text-destructive py-8">{departmentsError}</p>
              ) : (
                <>
                  <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                    {departments.map((dept) => (
                      <Button
                        key={dept.id}
                        variant={selectedDepartment === String(dept.id) ? "default" : "outline"}
                        className="h-16 text-base"
                        onClick={() => {
                          setSelectedDepartment(String(dept.id))
                          setSelectedDoctor("")
                        }}
                      >
                        {dept.name}
                      </Button>
                    ))}
                  </div>
                  <div className="mt-6 flex justify-end">
                    <Button
                      onClick={() => setStep("doctor")}
                      disabled={!selectedDepartment}
                    >
                      次へ
                    </Button>
                  </div>
                </>
              )}
            </CardContent>
          </Card>
        )}

        {/* Step 2: Select Doctor */}
        {step === "doctor" && (
          <Card>
            <CardHeader className="text-center">
              <CardTitle className="flex items-center justify-center gap-2">
                <User className="h-5 w-5" />
                担当医を選択
              </CardTitle>
              <CardDescription>
                {selectedDepartmentData?.name}の医師をお選びください
              </CardDescription>
            </CardHeader>
            <CardContent>
              {doctorsLoading ? (
                <div className="flex items-center justify-center py-12">
                  <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
                </div>
              ) : doctorsError ? (
                <p className="text-center text-destructive py-8">{doctorsError}</p>
              ) : doctors.length === 0 ? (
                <p className="text-center text-muted-foreground py-8">医師がいません</p>
              ) : (
                <div className="space-y-3">
                  {doctors.map((doc) => (
                    <button
                      key={doc.id}
                      type="button"
                      className={`w-full p-4 rounded-lg border text-left transition-colors ${
                        selectedDoctor === String(doc.id)
                          ? "border-primary bg-primary/5"
                          : "border-border hover:border-primary/50"
                      }`}
                      onClick={() => setSelectedDoctor(String(doc.id))}
                    >
                      <div className="font-medium text-foreground">{doc.name}</div>
                    </button>
                  ))}
                </div>
              )}
              <div className="mt-6 flex justify-between">
                <Button variant="outline" onClick={goBack}>
                  <ArrowLeft className="h-4 w-4 mr-2" />
                  戻る
                </Button>
                <Button onClick={() => setStep("datetime")} disabled={!selectedDoctor}>
                  次へ
                </Button>
              </div>
            </CardContent>
          </Card>
        )}

        {/* Step 3: Select Date & Time */}
        {step === "datetime" && (
          <Card>
            <CardHeader className="text-center">
              <CardTitle className="flex items-center justify-center gap-2">
                <Calendar className="h-5 w-5" />
                日時を選択
              </CardTitle>
              <CardDescription>ご希望の日時をお選びください</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-6">
                <div className="space-y-2">
                  <Label htmlFor="date">予約日</Label>
                  <Input
                    id="date"
                    type="date"
                    min={today}
                    max={maxDate}
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                  />
                </div>

                {selectedDate && (
                  <div className="space-y-2">
                    <Label>予約時間</Label>
                    {timeSlotsLoading ? (
                      <div className="flex justify-center py-8">
                        <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
                      </div>
                    ) : timeSlotsError ? (
                      <p className="text-destructive py-4">{timeSlotsError}</p>
                    ) : availableTimeSlots.length === 0 ? (
                      <p className="text-muted-foreground py-4">この日は空き枠がありません</p>
                    ) : (
                      <div className="grid grid-cols-4 md:grid-cols-5 gap-2">
                        {availableTimeSlots.map((slot) => (
                          <Button
                            key={slot.timeSlotId}
                            variant={selectedTimeSlot?.timeSlotId === slot.timeSlotId ? "default" : "outline"}
                            size="sm"
                            onClick={() => setSelectedTimeSlot(slot)}
                          >
                            {slot.startTime}
                          </Button>
                        ))}
                      </div>
                    )}
                  </div>
                )}
              </div>
              <div className="mt-6 flex justify-between">
                <Button variant="outline" onClick={goBack}>
                  <ArrowLeft className="h-4 w-4 mr-2" />
                  戻る
                </Button>
                <Button
                  onClick={() => setStep("info")}
                  disabled={!selectedDate || !selectedTimeSlot}
                >
                  次へ
                </Button>
              </div>
            </CardContent>
          </Card>
        )}

        {/* Step 4: Patient Info */}
        {step === "info" && (
          <Card>
            <CardHeader className="text-center">
              <CardTitle className="flex items-center justify-center gap-2">
                <User className="h-5 w-5" />
                患者情報
              </CardTitle>
              <CardDescription>ご連絡先をご入力ください</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="space-y-2">
                  <Label htmlFor="name">お名前 *</Label>
                  <Input
                    id="name"
                    placeholder="山田 太郎"
                    value={patientName}
                    onChange={(e) => setPatientName(e.target.value)}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="phone">電話番号 *</Label>
                  <Input
                    id="phone"
                    type="tel"
                    placeholder="090-1234-5678"
                    value={patientPhone}
                    onChange={(e) => setPatientPhone(e.target.value)}
                  />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="notes">症状・ご要望（任意）</Label>
                  <Textarea
                    id="notes"
                    placeholder="症状やご要望があればご記入ください"
                    value={notes}
                    onChange={(e) => setNotes(e.target.value)}
                    rows={3}
                  />
                </div>
              </div>
              <div className="mt-6 flex justify-between">
                <Button variant="outline" onClick={goBack}>
                  <ArrowLeft className="h-4 w-4 mr-2" />
                  戻る
                </Button>
                <Button
                  onClick={() => setStep("confirm")}
                  disabled={!patientName || !patientPhone}
                >
                  次へ
                </Button>
              </div>
            </CardContent>
          </Card>
        )}

        {/* Step 5: Confirmation */}
        {step === "confirm" && (
          <Card>
            <CardHeader className="text-center">
              <CardTitle className="flex items-center justify-center gap-2">
                <CheckCircle2 className="h-5 w-5" />
                予約内容の確認
              </CardTitle>
              <CardDescription>以下の内容でよろしければ予約を確定してください</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="bg-muted/50 rounded-lg p-4 space-y-3">
                <div className="flex justify-between py-2 border-b border-border">
                  <span className="text-muted-foreground">診療科</span>
                  <span className="font-medium text-foreground">{selectedDepartmentData?.name}</span>
                </div>
                <div className="flex justify-between py-2 border-b border-border">
                  <span className="text-muted-foreground">担当医</span>
                  <span className="font-medium text-foreground">{selectedDoctorData?.name}</span>
                </div>
                <div className="flex justify-between py-2 border-b border-border">
                  <span className="text-muted-foreground">予約日</span>
                  <span className="font-medium text-foreground">
                    {selectedDate && new Date(selectedDate).toLocaleDateString("ja-JP", {
                      year: "numeric",
                      month: "long",
                      day: "numeric",
                      weekday: "short",
                    })}
                  </span>
                </div>
                <div className="flex justify-between py-2 border-b border-border">
                  <span className="text-muted-foreground">予約時間</span>
                  <span className="font-medium text-foreground">{selectedTimeSlot?.startTime}</span>
                </div>
                <div className="flex justify-between py-2 border-b border-border">
                  <span className="text-muted-foreground">お名前</span>
                  <span className="font-medium text-foreground">{patientName}</span>
                </div>
                <div className="flex justify-between py-2 border-b border-border">
                  <span className="text-muted-foreground">電話番号</span>
                  <span className="font-medium text-foreground">{patientPhone}</span>
                </div>
                {notes && (
                  <div className="flex justify-between py-2">
                    <span className="text-muted-foreground">症状・ご要望</span>
                    <span className="font-medium text-foreground text-right max-w-[200px]">{notes}</span>
                  </div>
                )}
              </div>
              {submitError && (
                <p className="mt-4 text-destructive text-sm">{submitError}</p>
              )}
              <div className="mt-6 flex justify-between">
                <Button variant="outline" onClick={goBack} disabled={submitLoading}>
                  <ArrowLeft className="h-4 w-4 mr-2" />
                  戻る
                </Button>
                <Button onClick={handleSubmit} disabled={submitLoading}>
                  {submitLoading ? (
                    <>
                      <Loader2 className="h-4 w-4 mr-2 animate-spin" />
                      予約処理中...
                    </>
                  ) : (
                    "予約を確定する"
                  )}
                </Button>
              </div>
            </CardContent>
          </Card>
        )}

        {/* Step 6: Complete */}
        {step === "complete" && (
          <Card>
            <CardContent className="pt-8 text-center">
              <div className="w-16 h-16 mx-auto rounded-full bg-accent flex items-center justify-center mb-4">
                <CheckCircle2 className="h-8 w-8 text-accent-foreground" />
              </div>
              <h2 className="text-2xl font-semibold text-foreground mb-2">予約を受け付けました</h2>
              <p className="text-muted-foreground mb-6">
                ご予約内容の確認メールをお送りしました。<br />
                ご来院をお待ちしております。
              </p>
              <div className="bg-muted/50 rounded-lg p-4 mb-6 text-left">
                <div className="grid grid-cols-2 gap-2 text-sm">
                  <span className="text-muted-foreground">予約番号</span>
                  <span className="font-medium text-foreground">
                    {createdReservation
                      ? `RES-${String(createdReservation.reservationId).padStart(6, "0")}`
                      : `RES-${Date.now().toString().slice(-8)}`}
                  </span>
                  <span className="text-muted-foreground">予約日時</span>
                  <span className="font-medium text-foreground">
                    {selectedDate && new Date(selectedDate).toLocaleDateString("ja-JP")} {selectedTimeSlot?.startTime}
                  </span>
                  <span className="text-muted-foreground">診療科</span>
                  <span className="font-medium text-foreground">{selectedDepartmentData?.name}</span>
                  <span className="text-muted-foreground">担当医</span>
                  <span className="font-medium text-foreground">{selectedDoctorData?.name}</span>
                </div>
              </div>
              <div className="flex flex-col gap-3">
                <Button onClick={resetForm}>
                  別の予約をする
                </Button>
                <Link href="/">
                  <Button variant="outline" className="w-full bg-transparent">
                    トップページへ
                  </Button>
                </Link>
              </div>
            </CardContent>
          </Card>
        )}

        {/* Clinic Info */}
        {step !== "complete" && (
          <div className="mt-8 text-center text-sm text-muted-foreground">
            <p>ご不明な点がございましたら、お電話でお問い合わせください。</p>
            <p className="font-medium text-foreground mt-1">TEL: 03-1234-5678</p>
            <p className="mt-1">受付時間: 平日 9:00〜18:00</p>
          </div>
        )}
      </main>
    </div>
  )
}
