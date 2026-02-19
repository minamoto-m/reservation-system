"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { authApi } from "@/lib/api/auth"
import { reservationApi, type ReservationResponse } from "@/lib/api/reservation"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Loader2, LogOut, Calendar, User } from "lucide-react"

export default function MypagePage() {
  const [user, setUser] = useState<{ username: string } | null>(null)
  const [reservations, setReservations] = useState<ReservationResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [listError, setListError] = useState<string | null>(null)
  const [cancellingId, setCancellingId] = useState<number | null>(null)

  useEffect(() => {
    authApi
      .check()
      .then((u) => {
        setUser(u)
        return reservationApi.list()
      })
      .then(setReservations)
      .catch((err) => setListError(err instanceof Error ? err.message : "読み込みに失敗しました"))
      .finally(() => setLoading(false))
  }, [])

  const handleLogout = async () => {
    await authApi.logout()
    window.location.href = "/login"
  }

  const handleCancel = async (reservationId: number) => {
    setCancellingId(reservationId)
    try {
      await reservationApi.cancel(reservationId)
      setReservations((prev) => prev.filter((r) => r.reservationId !== reservationId))
    } finally {
      setCancellingId(null)
    }
  }

  const formatDate = (d: string) => d
  const formatTime = (t: string) => t

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-background">
      <header className="border-b bg-card">
        <div className="max-w-2xl mx-auto px-4 py-4 flex items-center justify-between">
          <h1 className="text-lg font-semibold">マイページ</h1>
          <Button variant="ghost" size="sm" onClick={handleLogout} className="gap-1">
            <LogOut className="h-4 w-4" />
            ログアウト
          </Button>
        </div>
      </header>

      <main className="max-w-2xl mx-auto px-4 py-6 space-y-6">
        {user && (
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-base flex items-center gap-2">
                <User className="h-4 w-4" />
                ログイン中
              </CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-sm text-muted-foreground">{user.username}</p>
            </CardContent>
          </Card>
        )}

        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Calendar className="h-5 w-5" />
              予約一覧
            </CardTitle>
            <CardDescription>ご予約の確認・キャンセルができます</CardDescription>
          </CardHeader>
          <CardContent>
            {listError && (
              <p className="text-sm text-destructive">{listError}</p>
            )}
            {!listError && reservations.length === 0 && (
              <p className="text-sm text-muted-foreground">予約はありません</p>
            )}
            {!listError && reservations.length > 0 && (
              <ul className="space-y-3">
                {reservations.map((r) => (
                  <li
                    key={r.reservationId}
                    className="flex items-center justify-between gap-4 p-3 rounded-lg border bg-card"
                  >
                    <div className="min-w-0">
                      <p className="font-medium truncate">{r.name}</p>
                      <p className="text-sm text-muted-foreground">
                        {formatDate(r.date)} {formatTime(r.startTime)}〜{formatTime(r.endTime)}
                      </p>
                    </div>
                    {r.status === "CONFIRMED" && (
                      <Button
                        variant="outline"
                        size="sm"
                        disabled={cancellingId === r.reservationId}
                        onClick={() => handleCancel(r.reservationId)}
                      >
                        {cancellingId === r.reservationId ? (
                          <Loader2 className="h-4 w-4 animate-spin" />
                        ) : (
                          "キャンセル"
                        )}
                      </Button>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </CardContent>
        </Card>

        <div className="flex flex-col sm:flex-row gap-3">
          <Link href="/" className="flex-1">
            <Button variant="outline" className="w-full">トップへ</Button>
          </Link>
          <Link href="/booking" className="flex-1">
            <Button className="w-full">新規予約</Button>
          </Link>
        </div>
      </main>
    </div>
  )
}
