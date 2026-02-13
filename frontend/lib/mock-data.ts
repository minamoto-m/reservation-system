import type { Reservation, Doctor, Department } from "./types"

export const departments: Department[] = [
  { id: "dept-1", name: "内科" },
  { id: "dept-2", name: "外科" },
  { id: "dept-3", name: "小児科" },
  { id: "dept-4", name: "皮膚科" },
  { id: "dept-5", name: "眼科" },
]

export const doctors: Doctor[] = [
  { id: "doc-1", name: "田中 太郎", departmentId: "dept-1", specialization: "一般内科" },
  { id: "doc-2", name: "佐藤 花子", departmentId: "dept-1", specialization: "循環器内科" },
  { id: "doc-3", name: "鈴木 一郎", departmentId: "dept-2", specialization: "消化器外科" },
  { id: "doc-4", name: "高橋 美咲", departmentId: "dept-3", specialization: "小児科一般" },
  { id: "doc-5", name: "伊藤 健太", departmentId: "dept-4", specialization: "一般皮膚科" },
  { id: "doc-6", name: "渡辺 優子", departmentId: "dept-5", specialization: "一般眼科" },
]

export const timeSlots = [
  "09:00",
  "09:30",
  "10:00",
  "10:30",
  "11:00",
  "11:30",
  "14:00",
  "14:30",
  "15:00",
  "15:30",
  "16:00",
  "16:30",
  "17:00",
]

const today = new Date()
const formatDate = (date: Date) => date.toISOString().split("T")[0]

export const initialReservations: Reservation[] = [
  {
    id: "res-1",
    patientName: "山田 一郎",
    patientPhone: "090-1234-5678",
    doctorId: "doc-1",
    departmentId: "dept-1",
    date: formatDate(today),
    time: "09:00",
    status: "confirmed",
    notes: "定期検診",
    createdAt: new Date(today.getTime() - 86400000).toISOString(),
  },
  {
    id: "res-2",
    patientName: "小林 美香",
    patientPhone: "080-2345-6789",
    doctorId: "doc-2",
    departmentId: "dept-1",
    date: formatDate(today),
    time: "10:30",
    status: "pending",
    notes: "胸の痛み",
    createdAt: new Date(today.getTime() - 43200000).toISOString(),
  },
  {
    id: "res-3",
    patientName: "中村 健一",
    patientPhone: "070-3456-7890",
    doctorId: "doc-3",
    departmentId: "dept-2",
    date: formatDate(new Date(today.getTime() + 86400000)),
    time: "14:00",
    status: "confirmed",
    createdAt: new Date(today.getTime() - 172800000).toISOString(),
  },
  {
    id: "res-4",
    patientName: "木村 さくら",
    patientPhone: "090-4567-8901",
    doctorId: "doc-4",
    departmentId: "dept-3",
    date: formatDate(new Date(today.getTime() + 86400000)),
    time: "09:30",
    status: "confirmed",
    notes: "予防接種",
    createdAt: new Date(today.getTime() - 259200000).toISOString(),
  },
  {
    id: "res-5",
    patientName: "加藤 大輔",
    patientPhone: "080-5678-9012",
    doctorId: "doc-5",
    departmentId: "dept-4",
    date: formatDate(new Date(today.getTime() + 172800000)),
    time: "15:30",
    status: "pending",
    notes: "肌荒れの相談",
    createdAt: new Date(today.getTime() - 86400000).toISOString(),
  },
]
