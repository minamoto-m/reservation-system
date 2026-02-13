"use client"

import { useState, useCallback } from "react"
import type { Reservation } from "@/lib/types"
import { initialReservations } from "@/lib/mock-data"

export function useReservations() {
  const [reservations, setReservations] = useState<Reservation[]>(initialReservations)

  const addReservation = useCallback((reservation: Omit<Reservation, "id" | "createdAt">) => {
    const newReservation: Reservation = {
      ...reservation,
      id: `res-${Date.now()}`,
      createdAt: new Date().toISOString(),
    }
    setReservations((prev) => [...prev, newReservation])
    return newReservation
  }, [])

  const updateReservation = useCallback((id: string, updates: Partial<Reservation>) => {
    setReservations((prev) =>
      prev.map((res) => (res.id === id ? { ...res, ...updates } : res))
    )
  }, [])

  const deleteReservation = useCallback((id: string) => {
    setReservations((prev) => prev.filter((res) => res.id !== id))
  }, [])

  const getReservationsByDate = useCallback(
    (date: string) => {
      return reservations.filter((res) => res.date === date)
    },
    [reservations]
  )

  return {
    reservations,
    addReservation,
    updateReservation,
    deleteReservation,
    getReservationsByDate,
  }
}
