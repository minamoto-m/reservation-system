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

  /**
   * 予約一覧を取得する（CONFIRMED のもの）
   */
  list: async (): Promise<ReservationResponse[]> => {
    return fetchApi<ReservationResponse[]>('/v1/reservations');
  },

  /**
   * 予約をキャンセルする
   */
  cancel: async (reservationId: number): Promise<{ reservationId: number; status: string }> => {
    return fetchApi<{ reservationId: number; status: string }>(
      `/v1/reservations/${reservationId}/cancel`,
      { method: 'PATCH' }
    );
  },
};
