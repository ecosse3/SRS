import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClassRoom } from 'app/shared/model/class-room.model';

type EntityResponseType = HttpResponse<IClassRoom>;
type EntityArrayResponseType = HttpResponse<IClassRoom[]>;

@Injectable({ providedIn: 'root' })
export class ClassRoomService {
  public resourceUrl = SERVER_API_URL + 'api/class-rooms';

  constructor(protected http: HttpClient) {}

  create(classRoom: IClassRoom): Observable<EntityResponseType> {
    return this.http.post<IClassRoom>(this.resourceUrl, classRoom, { observe: 'response' });
  }

  update(classRoom: IClassRoom): Observable<EntityResponseType> {
    return this.http.put<IClassRoom>(this.resourceUrl, classRoom, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassRoom>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassRoom[]>(`${this.resourceUrl}?size=150`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClassRoomsByBuildingId(buildingId: number): Observable<EntityArrayResponseType> {
    return this.http.get<IClassRoom[]>(`${this.resourceUrl}?buildingId.equals=${buildingId}&size=100`, { observe: 'response' });
  }
}
