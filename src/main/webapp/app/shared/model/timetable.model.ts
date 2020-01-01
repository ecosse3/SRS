import { Moment } from 'moment';
import { ISchoolGroup } from 'app/shared/model/school-group.model';
import { IBuilding } from 'app/shared/model/building.model';
import { IClassRoom } from 'app/shared/model/class-room.model';
import { IClassHours } from 'app/shared/model/class-hours.model';
import { IClassDuration } from 'app/shared/model/class-duration.model';

export interface ITimetable {
  id?: number;
  subject?: string;
  classDate?: Moment;
  schoolGroup?: ISchoolGroup;
  building?: IBuilding;
  classRoom?: IClassRoom;
  startTime?: IClassHours;
  classDuration?: IClassDuration;
  endTime?: IClassHours;
}

export class Timetable implements ITimetable {
  constructor(
    public id?: number,
    public subject?: string,
    public classDate?: Moment,
    public schoolGroup?: ISchoolGroup,
    public building?: IBuilding,
    public classRoom?: IClassRoom,
    public startTime?: IClassHours,
    public classDuration?: IClassDuration,
    public endTime?: IClassHours
  ) {}
}
