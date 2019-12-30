import { IUser } from 'app/core/user/user.model';
import { IReservation } from 'app/shared/model/reservation.model';
import { IMajor } from 'app/shared/model/major.model';
import { ITimetable } from 'app/shared/model/timetable.model';

export interface ISchoolGroup {
  id?: number;
  name?: string;
  starost?: IUser;
  reservationS?: IReservation[];
  major?: IMajor;
  timetables?: ITimetable[];
}

export class SchoolGroup implements ISchoolGroup {
  constructor(
    public id?: number,
    public name?: string,
    public starost?: IUser,
    public reservationS?: IReservation[],
    public major?: IMajor,
    public timetables?: ITimetable[]
  ) {}
}
