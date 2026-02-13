import { CalendarDays } from "lucide-react"

export function Header() {
  return (
    <header className="border-b border-border bg-card">
      <div className="mx-auto flex max-w-7xl items-center gap-3 px-4 py-4 sm:px-6">
        <div className="flex h-10 w-10 items-center justify-center rounded-lg bg-primary">
          <CalendarDays className="h-5 w-5 text-primary-foreground" />
        </div>
        <div>
          <h1 className="text-lg font-semibold text-card-foreground">
            クリニック予約管理
          </h1>
          <p className="text-sm text-muted-foreground">予約の作成・管理・確認</p>
        </div>
      </div>
    </header>
  )
}
