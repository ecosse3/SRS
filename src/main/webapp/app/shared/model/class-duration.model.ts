import { IReservation } from 'app/shared/model/reservation.model';

export interface IClassDuration {
  id?: number;
  name?: string;
  reservations?: IReservation[];
}

export class ClassDuration implements IClassDuration {
  constructor(public id?: number, public name?: string, public reservations?: IReservation[]) {}
}
