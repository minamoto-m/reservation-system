"use client"

import { useMemo } from "react"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { MoreHorizontal, Check, X, Clock } from "lucide-react"
import { doctors, departments } from "@/lib/mock-data"
import type { Reservation } from "@/lib/types"

interface ReservationListProps {
  reservations: Reservation[]
  onUpdateStatus: (id: string, status: Reservation["status"]) => void
  onDelete: (id: string) => void
}

const statusConfig = {
  confirmed: {
    label: "確定",
    variant: "default" as const,
    icon: Check,
    className: "bg-accent text-accent-foreground hover:bg-accent/80",
  },
  pending: {
    label: "保留中",
    variant: "secondary" as const,
    icon: Clock,
    className: "bg-chart-4/20 text-chart-4 hover:bg-chart-4/30",
  },
  cancelled: {
    label: "キャンセル",
    variant: "destructive" as const,
    icon: X,
    className: "bg-destructive/20 text-destructive hover:bg-destructive/30",
  },
}

export function ReservationList({
  reservations,
  onUpdateStatus,
  onDelete,
}: ReservationListProps) {
  const sortedReservations = useMemo(() => {
    return [...reservations].sort((a, b) => {
      const dateCompare = a.date.localeCompare(b.date)
      if (dateCompare !== 0) return dateCompare
      return a.time.localeCompare(b.time)
    })
  }, [reservations])

  const getDoctorName = (doctorId: string) => {
    const doctor = doctors.find((d) => d.id === doctorId)
    return doctor?.name || "未設定"
  }

  const getDepartmentName = (departmentId: string) => {
    const department = departments.find((d) => d.id === departmentId)
    return department?.name || "未設定"
  }

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr)
    return date.toLocaleDateString("ja-JP", {
      month: "short",
      day: "numeric",
      weekday: "short",
    })
  }

  if (reservations.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center rounded-lg border border-dashed border-border py-16">
        <p className="text-muted-foreground">予約がありません</p>
        <p className="mt-1 text-sm text-muted-foreground">
          新規予約ボタンから予約を追加してください
        </p>
      </div>
    )
  }

  return (
    <div className="overflow-hidden rounded-lg border border-border bg-card">
      <Table>
        <TableHeader>
          <TableRow className="bg-muted/50">
            <TableHead className="font-semibold">患者名</TableHead>
            <TableHead className="font-semibold">日時</TableHead>
            <TableHead className="font-semibold">診療科</TableHead>
            <TableHead className="font-semibold">担当医</TableHead>
            <TableHead className="font-semibold">ステータス</TableHead>
            <TableHead className="w-12"></TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {sortedReservations.map((reservation) => {
            const status = statusConfig[reservation.status]
            const StatusIcon = status.icon
            return (
              <TableRow key={reservation.id}>
                <TableCell>
                  <div>
                    <p className="font-medium text-card-foreground">
                      {reservation.patientName}
                    </p>
                    <p className="text-sm text-muted-foreground">
                      {reservation.patientPhone}
                    </p>
                  </div>
                </TableCell>
                <TableCell>
                  <div>
                    <p className="font-medium text-card-foreground">
                      {formatDate(reservation.date)}
                    </p>
                    <p className="text-sm text-muted-foreground">{reservation.time}</p>
                  </div>
                </TableCell>
                <TableCell className="text-card-foreground">
                  {getDepartmentName(reservation.departmentId)}
                </TableCell>
                <TableCell className="text-card-foreground">
                  {getDoctorName(reservation.doctorId)}
                </TableCell>
                <TableCell>
                  <Badge className={status.className}>
                    <StatusIcon className="mr-1 h-3 w-3" />
                    {status.label}
                  </Badge>
                </TableCell>
                <TableCell>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" size="icon" className="h-8 w-8">
                        <MoreHorizontal className="h-4 w-4" />
                        <span className="sr-only">メニューを開く</span>
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      {reservation.status !== "confirmed" && (
                        <DropdownMenuItem
                          onClick={() => onUpdateStatus(reservation.id, "confirmed")}
                        >
                          <Check className="mr-2 h-4 w-4 text-accent" />
                          確定する
                        </DropdownMenuItem>
                      )}
                      {reservation.status !== "pending" && (
                        <DropdownMenuItem
                          onClick={() => onUpdateStatus(reservation.id, "pending")}
                        >
                          <Clock className="mr-2 h-4 w-4 text-chart-4" />
                          保留にする
                        </DropdownMenuItem>
                      )}
                      {reservation.status !== "cancelled" && (
                        <DropdownMenuItem
                          onClick={() => onUpdateStatus(reservation.id, "cancelled")}
                        >
                          <X className="mr-2 h-4 w-4 text-destructive" />
                          キャンセル
                        </DropdownMenuItem>
                      )}
                      <DropdownMenuItem
                        onClick={() => onDelete(reservation.id)}
                        className="text-destructive focus:text-destructive"
                      >
                        削除
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            )
          })}
        </TableBody>
      </Table>
    </div>
  )
}
