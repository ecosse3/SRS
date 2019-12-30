import { IReservation } from 'app/shared/model/reservation.model';
import { IClassModel } from 'app/shared/model/class-model.model';
import { IBuilding } from 'app/shared/model/building.model';
import { ITimetable } from 'app/shared/model/timetable.model';

export interface IClassRoom {
  id?: number;
  number?: string;
  maximumSize?: number;
  reservationCS?: IReservation[];
  classModels?: IClassModel[];
  building?: IBuilding;
  timetables?: ITimetable[];
}

export class ClassRoom implements IClassRoom {
  constructor(
    public id?: number,
    public number?: string,
    public maximumSize?: number,
    public reservationCS?: IReservation[],
    public classModels?: IClassModel[],
    public building?: IBuilding,
    public timetables?: ITimetable[]
  ) {}
}
