import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { ISchoolGroup } from 'app/shared/model/school-group.model';
import { IBuilding } from 'app/shared/model/building.model';
import { IClassRoom } from 'app/shared/model/class-room.model';
import { IClassHours } from 'app/shared/model/class-hours.model';
import { IClassDuration } from 'app/shared/model/class-duration.model';

export interface IReservation {
  id?: number;
  name?: string;
  noteToTeacher?: string;
  originalClassDate?: Moment;
  newClassDate?: Moment;
  requestedBy?: string;
  participants?: IUser[];
  schoolGroup?: ISchoolGroup;
  building?: IBuilding;
  classRoom?: IClassRoom;
  originalStartTime?: IClassHours;
  newStartTime?: IClassHours;
  classDuration?: IClassDuration;
}

export class Reservation implements IReservation {
  constructor(
    public id?: number,
    public name?: string,
    public noteToTeacher?: string,
    public originalClassDate?: Moment,
    public newClassDate?: Moment,
    public requestedBy?: string,
    public participants?: IUser[],
    public schoolGroup?: ISchoolGroup,
    public building?: IBuilding,
    public classRoom?: IClassRoom,
    public originalStartTime?: IClassHours,
    public newStartTime?: IClassHours,
    public classDuration?: IClassDuration
  ) {}
}
