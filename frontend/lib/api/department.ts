import { fetchApi } from '@/lib/api-client';
import type { Department, CreateDepartmentDto, UpdateDepartmentDto } from '@/types/department';

/**
 * Department API
 */
export const departmentApi = {
  /**
   * 全件取得
   */
  getAll: async (): Promise<Department[]> => {
    return fetchApi<Department[]>('/v1/departments');
  },

  /**
   * ID指定で取得
   */
  getById: async (id: number): Promise<Department> => {
    return fetchApi<Department>(`/v1/departments/${id}`);
  },

  /**
   * 新規作成
   */
  create: async (data: CreateDepartmentDto): Promise<Department> => {
    return fetchApi<Department>('/v1/departments', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  /**
   * 更新
   */
  update: async (id: number, data: UpdateDepartmentDto): Promise<Department> => {
    return fetchApi<Department>(`/v1/departments/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  /**
   * 削除
   */
  delete: async (id: number): Promise<void> => {
    return fetchApi<void>(`/v1/departments/${id}`, {
      method: 'DELETE',
    });
  },
};