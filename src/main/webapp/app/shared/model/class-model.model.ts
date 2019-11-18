import { IClassRoom } from 'app/shared/model/class-room.model';

export interface IClassModel {
  id?: number;
  name?: string;
  classRooms?: IClassRoom[];
}

export class ClassModel implements IClassModel {
  constructor(public id?: number, public name?: string, public classRooms?: IClassRoom[]) {}
}
