export interface IMyReservations {
  id?: number;
}

export class MyReservations implements IMyReservations {
  constructor(public id?: number) {}
}
