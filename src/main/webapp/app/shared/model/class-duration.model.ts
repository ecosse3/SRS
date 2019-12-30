import { IReservation } from 'app/shared/model/reservation.model';
import { ITimetable } from 'app/shared/model/timetable.model';

export interface IClassDuration {
  id?: number;
  name?: string;
  reservations?: IReservation[];
  timetables?: ITimetable[];
}

export class ClassDuration implements IClassDuration {
  constructor(public id?: number, public name?: string, public reservations?: IReservation[], public timetables?: ITimetable[]) {}
}
