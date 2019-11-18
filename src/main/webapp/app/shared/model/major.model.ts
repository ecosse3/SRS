import { ISchoolGroup } from 'app/shared/model/school-group.model';
import { IDepartment } from 'app/shared/model/department.model';

export interface IMajor {
  id?: number;
  name?: string;
  schoolGroups?: ISchoolGroup[];
  department?: IDepartment;
}

export class Major implements IMajor {
  constructor(public id?: number, public name?: string, public schoolGroups?: ISchoolGroup[], public department?: IDepartment) {}
}
