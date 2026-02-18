import { fetchApi } from '@/lib/api-client';

export type ReservationCreateRequest = {
  timeSlotId: number;
  name: string;
  phoneNumber: string;
};

export type ReservationResponse = {
  reservationId: number;
  timeSlotId: number;
  date: string;  // "yyyy-MM-dd"
  startTime: string;  // "HH:mm"
  endTime: string;  // "HH:mm"
  status: string;
  name: string;
};

/**
 * 予約 API
 */
export const reservationApi = {
  /**
   * 予約を作成する
   */
  create: async (request: ReservationCreateRequest): Promise<ReservationResponse> => {
    return fetchApi<ReservationResponse>('/v1/reservations', {
      method: 'POST',
      body: JSON.stringify(request),
    });
  },
};
