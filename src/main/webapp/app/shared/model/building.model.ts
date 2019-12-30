import { IClassRoom } from 'app/shared/model/class-room.model';
import { IReservation } from 'app/shared/model/reservation.model';
import { ITimetable } from 'app/shared/model/timetable.model';

export interface IBuilding {
  id?: number;
  number?: string;
  classRoomBS?: IClassRoom[];
  reservationBS?: IReservation[];
  timetables?: ITimetable[];
}

export class Building implements IBuilding {
  constructor(
    public id?: number,
    public number?: string,
    public classRoomBS?: IClassRoom[],
    public reservationBS?: IReservation[],
    public timetables?: ITimetable[]
  ) {}
}
