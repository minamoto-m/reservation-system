"use client"

import { CalendarDays, LogOut } from "lucide-react"
import Link from "next/link"
import { Button } from "@/components/ui/button"
import { authApi } from "@/lib/api/auth"
import { useRouter } from "next/navigation"

export function Header() {
  const router = useRouter()

  const handleLogout = async () => {
    try {
      await authApi.logout()
      router.push("/login")
      router.refresh()
    } catch {
      router.push("/login")
      router.refresh()
    }
  }

  return (
    <header className="border-b border-border bg-card">
      <div className="mx-auto flex max-w-7xl items-center justify-between gap-3 px-4 py-4 sm:px-6">
        <Link href="/admin" className="flex items-center gap-3 hover:opacity-90">
          <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary">
            <CalendarDays className="h-5 w-5 text-primary-foreground" />
          </div>
          <div>
            <h1 className="text-lg font-semibold text-card-foreground">
              クリニック予約管理
            </h1>
            <p className="text-sm text-muted-foreground">予約の作成・管理・確認</p>
          </div>
        </Link>
        <div className="flex items-center gap-2">
          <Link href="/booking">
            <Button variant="ghost" size="sm">
              予約ページ
            </Button>
          </Link>
          <Button variant="ghost" size="sm" onClick={handleLogout}>
            <LogOut className="h-4 w-4 mr-2" />
            ログアウト
          </Button>
        </div>
      </div>
    </header>
  )
}
