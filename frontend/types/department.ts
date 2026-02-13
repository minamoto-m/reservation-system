export interface Department {
    id: number;
    name: string;
  }
  
  export type CreateDepartmentDto = Omit<Department, 'id'>;
  export type UpdateDepartmentDto = Partial<CreateDepartmentDto>;