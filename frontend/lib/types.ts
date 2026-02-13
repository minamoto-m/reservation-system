export interface Reservation {
  id: string
  patientName: string
  patientPhone: string
  doctorId: string
  departmentId: string
  date: string
  time: string
  status: "confirmed" | "pending" | "cancelled"
  notes?: string
  createdAt: string
}

export interface Doctor {
  id: string
  name: string
  departmentId: string
  specialization: string
}

export interface Department {
  id: string
  name: string
}

export type ViewMode = "list" | "calendar"
