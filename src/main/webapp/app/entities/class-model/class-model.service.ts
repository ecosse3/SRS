import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClassModel } from 'app/shared/model/class-model.model';

type EntityResponseType = HttpResponse<IClassModel>;
type EntityArrayResponseType = HttpResponse<IClassModel[]>;

@Injectable({ providedIn: 'root' })
export class ClassModelService {
  public resourceUrl = SERVER_API_URL + 'api/class-models';

  constructor(protected http: HttpClient) {}

  create(classModel: IClassModel): Observable<EntityResponseType> {
    return this.http.post<IClassModel>(this.resourceUrl, classModel, { observe: 'response' });
  }

  update(classModel: IClassModel): Observable<EntityResponseType> {
    return this.http.put<IClassModel>(this.resourceUrl, classModel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassModel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
