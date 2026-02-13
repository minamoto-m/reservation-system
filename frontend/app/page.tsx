"use client"

import { useState, useMemo } from "react"
import { Header } from "@/components/header"
import { ReservationForm } from "@/components/reservation-form"
import { ReservationList } from "@/components/reservation-list"
import { ReservationCalendar } from "@/components/reservation-calendar"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Card, CardContent } from "@/components/ui/card"
import { List, Calendar, Search, Users, Clock, CheckCircle } from "lucide-react"
import { useReservations } from "@/hooks/use-reservations"
import type { ViewMode } from "@/lib/types"

export default function ReservationPage() {
  const {
    reservations,
    addReservation,
    updateReservation,
    deleteReservation,
  } = useReservations()

  const [viewMode, setViewMode] = useState<ViewMode>("list")
  const [searchQuery, setSearchQuery] = useState("")
  const [statusFilter, setStatusFilter] = useState<string>("all")

  const filteredReservations = useMemo(() => {
    return reservations.filter((res) => {
      const matchesSearch =
        searchQuery === "" ||
        res.patientName.toLowerCase().includes(searchQuery.toLowerCase()) ||
        res.patientPhone.includes(searchQuery)

      const matchesStatus =
        statusFilter === "all" || res.status === statusFilter

      return matchesSearch && matchesStatus
    })
  }, [reservations, searchQuery, statusFilter])

  const stats = useMemo(() => {
    const today = new Date().toISOString().split("T")[0]
    const todayReservations = reservations.filter((r) => r.date === today)
    return {
      total: reservations.length,
      today: todayReservations.length,
      confirmed: reservations.filter((r) => r.status === "confirmed").length,
      pending: reservations.filter((r) => r.status === "pending").length,
    }
  }, [reservations])

  const handleStatusUpdate = (id: string, status: "confirmed" | "pending" | "cancelled") => {
    updateReservation(id, { status })
  }

  return (
    <div className="min-h-screen bg-background">
      <Header />

      <main className="mx-auto max-w-7xl px-4 py-6 sm:px-6">
        {/* Stats Cards */}
        <div className="mb-6 grid grid-cols-2 gap-4 lg:grid-cols-4">
          <Card>
            <CardContent className="flex items-center gap-4 p-4">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary/10">
                <Users className="h-5 w-5 text-primary" />
              </div>
              <div>
                <p className="text-sm text-muted-foreground">総予約数</p>
                <p className="text-2xl font-semibold text-card-foreground">
                  {stats.total}
                </p>
              </div>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="flex items-center gap-4 p-4">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-accent/10">
                <Calendar className="h-5 w-5 text-accent" />
              </div>
              <div>
                <p className="text-sm text-muted-foreground">本日の予約</p>
                <p className="text-2xl font-semibold text-card-foreground">
                  {stats.today}
                </p>
              </div>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="flex items-center gap-4 p-4">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-accent/10">
                <CheckCircle className="h-5 w-5 text-accent" />
              </div>
              <div>
                <p className="text-sm text-muted-foreground">確定済み</p>
                <p className="text-2xl font-semibold text-card-foreground">
                  {stats.confirmed}
                </p>
              </div>
            </CardContent>
          </Card>
          <Card>
            <CardContent className="flex items-center gap-4 p-4">
              <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-chart-4/10">
                <Clock className="h-5 w-5 text-chart-4" />
              </div>
              <div>
                <p className="text-sm text-muted-foreground">保留中</p>
                <p className="text-2xl font-semibold text-card-foreground">
                  {stats.pending}
                </p>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Toolbar */}
        <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <div className="flex flex-1 flex-col gap-3 sm:flex-row sm:items-center">
            <div className="relative flex-1 sm:max-w-xs">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                placeholder="患者名・電話番号で検索"
                className="pl-9"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </div>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-full sm:w-36">
                <SelectValue placeholder="ステータス" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">すべて</SelectItem>
                <SelectItem value="confirmed">確定</SelectItem>
                <SelectItem value="pending">保留中</SelectItem>
                <SelectItem value="cancelled">キャンセル</SelectItem>
              </SelectContent>
            </Select>
          </div>

          <div className="flex items-center gap-3">
            <Tabs
              value={viewMode}
              onValueChange={(v) => setViewMode(v as ViewMode)}
            >
              <TabsList>
                <TabsTrigger value="list" className="gap-2">
                  <List className="h-4 w-4" />
                  <span className="hidden sm:inline">一覧</span>
                </TabsTrigger>
                <TabsTrigger value="calendar" className="gap-2">
                  <Calendar className="h-4 w-4" />
                  <span className="hidden sm:inline">カレンダー</span>
                </TabsTrigger>
              </TabsList>
            </Tabs>
            <ReservationForm onSubmit={addReservation} />
          </div>
        </div>

        {/* Content */}
        {viewMode === "list" ? (
          <ReservationList
            reservations={filteredReservations}
            onUpdateStatus={handleStatusUpdate}
            onDelete={deleteReservation}
          />
        ) : (
          <ReservationCalendar reservations={filteredReservations} />
        )}
      </main>
    </div>
  )
}
