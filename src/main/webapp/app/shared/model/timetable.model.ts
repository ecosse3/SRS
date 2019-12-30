import { Moment } from 'moment';
import { ISchoolGroup } from 'app/shared/model/school-group.model';
import { IBuilding } from 'app/shared/model/building.model';

export interface ITimetable {
  id?: number;
  subject?: string;
  classDate?: Moment;
  schoolGroup?: ISchoolGroup;
  building?: IBuilding;
}

export class Timetable implements ITimetable {
  constructor(
    public id?: number,
    public subject?: string,
    public classDate?: Moment,
    public schoolGroup?: ISchoolGroup,
    public building?: IBuilding
  ) {}
}
