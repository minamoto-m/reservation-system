import { fetchApi } from '@/lib/api-client';
import type { Doctor } from '@/types/doctor';

/**
 * Doctor API
 */
export const doctorApi = {
  /**
   * 全件取得
   */
  getAll: async (): Promise<Doctor[]> => {
    return fetchApi<Doctor[]>('/v1/doctors');
  },

  /**
   * 診療科ID指定で取得
   */
  getByDepartmentId: async (departmentId: number): Promise<Doctor[]> => {
    return fetchApi<Doctor[]>(`/v1/doctors/${departmentId}`);
  },
};
