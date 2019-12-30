import { Moment } from 'moment';
import { ISchoolGroup } from 'app/shared/model/school-group.model';

export interface ITimetable {
  id?: number;
  subject?: string;
  classDate?: Moment;
  schoolGroup?: ISchoolGroup;
}

export class Timetable implements ITimetable {
  constructor(public id?: number, public subject?: string, public classDate?: Moment, public schoolGroup?: ISchoolGroup) {}
}
