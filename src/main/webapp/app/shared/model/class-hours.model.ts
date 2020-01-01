import { IReservation } from 'app/shared/model/reservation.model';
import { ITimetable } from 'app/shared/model/timetable.model';

export interface IClassHours {
  id?: number;
  startTime?: string;
  endTime?: string;
  originalStartTimes?: IReservation[];
  newStartTimes?: IReservation[];
  timetables?: ITimetable[];
  tt_endTimes?: ITimetable[];
}

export class ClassHours implements IClassHours {
  constructor(
    public id?: number,
    public startTime?: string,
    public endTime?: string,
    public originalStartTimes?: IReservation[],
    public newStartTimes?: IReservation[],
    public timetables?: ITimetable[],
    public tt_endTimes?: ITimetable[]
  ) {}
}
