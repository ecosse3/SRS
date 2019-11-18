import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClassHours } from 'app/shared/model/class-hours.model';

type EntityResponseType = HttpResponse<IClassHours>;
type EntityArrayResponseType = HttpResponse<IClassHours[]>;

@Injectable({ providedIn: 'root' })
export class ClassHoursService {
  public resourceUrl = SERVER_API_URL + 'api/class-hours';

  constructor(protected http: HttpClient) {}

  create(classHours: IClassHours): Observable<EntityResponseType> {
    return this.http.post<IClassHours>(this.resourceUrl, classHours, { observe: 'response' });
  }

  update(classHours: IClassHours): Observable<EntityResponseType> {
    return this.http.put<IClassHours>(this.resourceUrl, classHours, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassHours>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassHours[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
