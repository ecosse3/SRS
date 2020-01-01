import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITimetable } from 'app/shared/model/timetable.model';

type EntityResponseType = HttpResponse<ITimetable>;
type EntityArrayResponseType = HttpResponse<ITimetable[]>;

@Injectable({ providedIn: 'root' })
export class TimetableService {
  public resourceUrl = SERVER_API_URL + 'api/timetables';

  constructor(protected http: HttpClient) {}

  create(timetable: ITimetable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timetable);
    return this.http
      .post<ITimetable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(timetable: ITimetable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(timetable);
    return this.http
      .put<ITimetable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITimetable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITimetable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(timetable: ITimetable): ITimetable {
    const copy: ITimetable = Object.assign({}, timetable, {
      classDate: timetable.classDate != null && timetable.classDate.isValid() ? timetable.classDate.format(DATE_FORMAT) : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.classDate = res.body.classDate != null ? moment(res.body.classDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((timetable: ITimetable) => {
        timetable.classDate = timetable.classDate != null ? moment(timetable.classDate) : null;
      });
    }
    return res;
  }

  checkIfClassRoomIsOccupied(
    buildingId: any,
    classRoomId: any,
    classDate: any,
    startTime: any,
    duration: any
  ): Observable<EntityArrayResponseType> {
    const endTime = startTime + duration - 1;

    return this.http.get<ITimetable[]>(
      `${this.resourceUrl}/count?buildingId.equals=${buildingId}&classRoomId.equals=${classRoomId}&classDate.equals=${classDate}&endTimeId.greaterThanOrEqual=${startTime}&startTimeId.lessThanOrEqual=${endTime}`,
      {
        observe: 'response'
      }
    );
  }
}
