import { fetchApi } from '@/lib/api-client';

export type TimeSlotAvailable = {
  timeSlotId: number;
  startTime: string;  // "HH:mm"
};

/**
 * 空き予約枠 API
 */
export const timeSlotApi = {
  /**
   * 医師IDと日付を指定して空き予約枠を取得
   */
  getAvailable: async (doctorId: number, date: string): Promise<TimeSlotAvailable[]> => {
    const params = new URLSearchParams({ doctorId: String(doctorId), date });
    return fetchApi<TimeSlotAvailable[]>(`/v1/timeslots?${params}`);
  },
};
