import { IMajor } from 'app/shared/model/major.model';

export interface IDepartment {
  id?: number;
  name?: string;
  majors?: IMajor[];
}

export class Department implements IDepartment {
  constructor(public id?: number, public name?: string, public majors?: IMajor[]) {}
}
