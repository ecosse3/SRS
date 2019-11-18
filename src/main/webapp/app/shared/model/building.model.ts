import { IClassRoom } from 'app/shared/model/class-room.model';
import { IReservation } from 'app/shared/model/reservation.model';

export interface IBuilding {
  id?: number;
  number?: string;
  classRoomBS?: IClassRoom[];
  reservationBS?: IReservation[];
}

export class Building implements IBuilding {
  constructor(public id?: number, public number?: string, public classRoomBS?: IClassRoom[], public reservationBS?: IReservation[]) {}
}
