import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBuilding } from 'app/shared/model/building.model';

type EntityResponseType = HttpResponse<IBuilding>;
type EntityArrayResponseType = HttpResponse<IBuilding[]>;

@Injectable({ providedIn: 'root' })
export class BuildingService {
  public resourceUrl = SERVER_API_URL + 'api/buildings';

  constructor(protected http: HttpClient) {}

  create(building: IBuilding): Observable<EntityResponseType> {
    return this.http.post<IBuilding>(this.resourceUrl, building, { observe: 'response' });
  }

  update(building: IBuilding): Observable<EntityResponseType> {
    return this.http.put<IBuilding>(this.resourceUrl, building, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBuilding>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBuilding[]>(`${this.resourceUrl}?size=100`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
