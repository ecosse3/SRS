import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISchoolGroup } from 'app/shared/model/school-group.model';

type EntityResponseType = HttpResponse<ISchoolGroup>;
type EntityArrayResponseType = HttpResponse<ISchoolGroup[]>;

@Injectable({ providedIn: 'root' })
export class SchoolGroupService {
  public resourceUrl = SERVER_API_URL + 'api/school-groups';

  constructor(protected http: HttpClient) {}

  create(schoolGroup: ISchoolGroup): Observable<EntityResponseType> {
    return this.http.post<ISchoolGroup>(this.resourceUrl, schoolGroup, { observe: 'response' });
  }

  update(schoolGroup: ISchoolGroup): Observable<EntityResponseType> {
    return this.http.put<ISchoolGroup>(this.resourceUrl, schoolGroup, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISchoolGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISchoolGroup[]>(`${this.resourceUrl}?size=100`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
