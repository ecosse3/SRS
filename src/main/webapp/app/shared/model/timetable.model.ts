import { Moment } from 'moment';
import { ISchoolGroup } from 'app/shared/model/school-group.model';
import { IBuilding } from 'app/shared/model/building.model';
import { IClassRoom } from 'app/shared/model/class-room.model';

export interface ITimetable {
  id?: number;
  subject?: string;
  classDate?: Moment;
  schoolGroup?: ISchoolGroup;
  building?: IBuilding;
  classRoom?: IClassRoom;
}

export class Timetable implements ITimetable {
  constructor(
    public id?: number,
    public subject?: string,
    public classDate?: Moment,
    public schoolGroup?: ISchoolGroup,
    public building?: IBuilding,
    public classRoom?: IClassRoom
  ) {}
}
