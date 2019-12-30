import { Moment } from 'moment';

export interface ITimetable {
  id?: number;
  subject?: string;
  classDate?: Moment;
}

export class Timetable implements ITimetable {
  constructor(public id?: number, public subject?: string, public classDate?: Moment) {}
}
