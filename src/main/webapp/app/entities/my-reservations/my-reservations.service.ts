import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMyReservations } from 'app/shared/model/my-reservations.model';
import { IReservation } from 'app/shared/model/reservation.model';

type EntityResponseType = HttpResponse<IReservation>;
type EntityArrayResponseType = HttpResponse<IReservation[]>;

@Injectable({ providedIn: 'root' })
export class MyReservationsService {
  public resourceUrl = SERVER_API_URL + 'api/reservations';

  constructor(protected http: HttpClient) {}

  create(myReservations: IReservation): Observable<EntityResponseType> {
    return this.http.post<IReservation>(this.resourceUrl, myReservations, { observe: 'response' });
  }

  update(myReservations: IReservation): Observable<EntityResponseType> {
    return this.http.put<IReservation>(this.resourceUrl, myReservations, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReservation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReservation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  cancel(myReservations: IReservation): Observable<EntityResponseType> {
    return this.http.put<IReservation>(
      this.resourceUrl,
      {
        ...myReservations,
        status: {
          id: 2,
          polishName: 'Anulowane',
          englishName: 'Canceled'
        }
      },
      { observe: 'response' }
    );
  }

  reject(myReservations: IReservation): Observable<EntityResponseType> {
    return this.http.put<IReservation>(
      this.resourceUrl,
      {
        ...myReservations,
        status: {
          id: 4,
          polishName: 'Odrzucone',
          englishName: 'Rejected'
        }
      },
      { observe: 'response' }
    );
  }

  accept(myReservations: IReservation): Observable<EntityResponseType> {
    return this.http.put<IReservation>(
      this.resourceUrl,
      {
        ...myReservations,
        status: {
          id: 3,
          polishName: 'Zaakceptowane',
          englishName: 'Accepted'
        }
      },
      { observe: 'response' }
    );
  }

  getCountUserReservationsByAccountName(currentAccount: String): Observable<EntityArrayResponseType> {
    return this.http.get<IReservation[]>(`${this.resourceUrl}/count?requestedBy.equals=${currentAccount}`, { observe: 'response' });
  }

  getUserReservationsByAccountName(currentAccount: String): Observable<EntityArrayResponseType> {
    return this.http.get<IReservation[]>(`${this.resourceUrl}?requestedBy.equals=${currentAccount}`, { observe: 'response' });
  }

  getTeacherReservationsByAccountName(currentAccount: String): Observable<EntityArrayResponseType> {
    return this.http.get<IReservation[]>(`${this.resourceUrl}?participants.equals=${currentAccount}`, { observe: 'response' });
  }
}
