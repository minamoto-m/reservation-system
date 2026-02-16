"use client"

import { useEffect, useState } from "react"
import { authApi } from "@/lib/api/auth"
import { Loader2 } from "lucide-react"

type ProtectedPageProps = {
  children: React.ReactNode
}

/**
 * 認証が必要なページをラップするコンポーネント。
 * 未認証の場合はログインページへリダイレクトする。
 */
export function ProtectedPage({ children }: ProtectedPageProps) {
  const [isChecking, setIsChecking] = useState(true)
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  useEffect(() => {
    authApi
      .check()
      .then(() => {
        setIsAuthenticated(true)
      })
      .catch(() => {
        // 401 時は fetchApi が自動で /login へリダイレクト
      })
      .finally(() => {
        setIsChecking(false)
      })
  }, [])

  if (isChecking) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
      </div>
    )
  }

  if (!isAuthenticated) {
    // リダイレクト中はローディングを表示（fetchApi が 401 で /login へリダイレクト）
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <Loader2 className="h-8 w-8 animate-spin text-muted-foreground" />
      </div>
    )
  }

  return <>{children}</>
}
