import { IReservation } from 'app/shared/model/reservation.model';

export interface IClassHours {
  id?: number;
  startTime?: string;
  endTime?: string;
  originalStartTimes?: IReservation[];
  newStartTimes?: IReservation[];
}

export class ClassHours implements IClassHours {
  constructor(
    public id?: number,
    public startTime?: string,
    public endTime?: string,
    public originalStartTimes?: IReservation[],
    public newStartTimes?: IReservation[]
  ) {}
}
