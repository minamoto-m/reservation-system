"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { Stethoscope, Calendar, LogOut } from "lucide-react"
import { Button } from "@/components/ui/button"
import { authApi } from "@/lib/api/auth"

/**
 * クリニックトップページ（将来的に案内・アクセス等を表示）
 */
export default function ClinicTopPage() {
  const [user, setUser] = useState<{ username: string } | null | undefined>(undefined)

  useEffect(() => {
    authApi.getCurrentUser().then(setUser)
  }, [])

  const handleLogout = async () => {
    await authApi.logout()
    setUser(null)
    window.location.href = "/"
  }

  return (
    <div className="min-h-screen bg-background flex flex-col">
      <header className="border-b bg-card">
        <div className="max-w-4xl mx-auto px-4 py-4 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-lg bg-primary flex items-center justify-center">
              <Stethoscope className="h-5 w-5 text-primary-foreground" />
            </div>
            <div>
              <h1 className="text-lg font-semibold text-foreground">メディカルクリニック</h1>
              <p className="text-sm text-muted-foreground">地域のかかりつけ医</p>
            </div>
          </div>
          <div className="flex gap-2">
            {user === undefined ? (
              <span className="text-sm text-muted-foreground">...</span>
            ) : user ? (
              <>
                <Link href="/mypage">
                  <Button variant="ghost" size="sm">マイページ</Button>
                </Link>
                <Button variant="ghost" size="sm" onClick={handleLogout} className="gap-1">
                  <LogOut className="h-4 w-4" />
                  ログアウト
                </Button>
              </>
            ) : (
              <>
                <Link href="/login">
                  <Button variant="ghost" size="sm">ログイン</Button>
                </Link>
                <Link href="/mypage">
                  <Button variant="ghost" size="sm">マイページ</Button>
                </Link>
              </>
            )}
          </div>
        </div>
      </header>

      <main className="flex-1 flex flex-col items-center justify-center px-4 py-16">
        <div className="text-center max-w-xl">
          <h2 className="text-2xl font-semibold text-foreground mb-4">
            オンライン予約
          </h2>
          <p className="text-muted-foreground mb-8">
            ご来院のご予約はこちらから。診療科・医師・日時をお選びいただけます。
          </p>
          <Link href="/booking">
            <Button size="lg" className="gap-2">
              <Calendar className="h-5 w-5" />
              予約する
            </Button>
          </Link>
        </div>

        <div className="mt-16 text-center text-sm text-muted-foreground">
          <p>ご不明な点がございましたら、お電話でお問い合わせください。</p>
          <p className="font-medium text-foreground mt-1">TEL: 03-1234-5678</p>
          <p className="mt-1">受付時間: 平日 9:00〜18:00</p>
        </div>
      </main>
    </div>
  )
}
