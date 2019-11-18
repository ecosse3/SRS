import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClassDuration } from 'app/shared/model/class-duration.model';

type EntityResponseType = HttpResponse<IClassDuration>;
type EntityArrayResponseType = HttpResponse<IClassDuration[]>;

@Injectable({ providedIn: 'root' })
export class ClassDurationService {
  public resourceUrl = SERVER_API_URL + 'api/class-durations';

  constructor(protected http: HttpClient) {}

  create(classDuration: IClassDuration): Observable<EntityResponseType> {
    return this.http.post<IClassDuration>(this.resourceUrl, classDuration, { observe: 'response' });
  }

  update(classDuration: IClassDuration): Observable<EntityResponseType> {
    return this.http.put<IClassDuration>(this.resourceUrl, classDuration, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassDuration>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassDuration[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
