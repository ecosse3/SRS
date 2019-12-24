import { IReservation } from 'app/shared/model/reservation.model';

export interface IStatus {
  id?: number;
  polishName?: string;
  englishName?: string;
  reservations?: IReservation[];
}

export class Status implements IStatus {
  constructor(public id?: number, public polishName?: string, public englishName?: string, public reservations?: IReservation[]) {}
}
