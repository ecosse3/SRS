import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MyReservations } from 'app/shared/model/my-reservations.model';
import { MyReservationsService } from './my-reservations.service';
import { MyReservationsComponent } from './my-reservations.component';
import { MyReservationsDetailComponent } from './my-reservations-detail.component';
import { MyReservationsDeletePopupComponent } from './my-reservations-delete-dialog.component';
import { MyReservationsRejectPopupComponent } from './my-reservations-reject-dialog.component';
import { MyReservationsAcceptPopupComponent } from './my-reservations-accept-dialog.component';
import { IMyReservations } from 'app/shared/model/my-reservations.model';

@Injectable({ providedIn: 'root' })
export class MyReservationsResolve implements Resolve<IMyReservations> {
  constructor(private service: MyReservationsService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMyReservations> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MyReservations>) => response.ok),
        map((myReservations: HttpResponse<MyReservations>) => myReservations.body)
      );
    }
    return of(new MyReservations());
  }
}

export const myReservationsRoute: Routes = [
  {
    path: '',
    component: MyReservationsComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.myReservations.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MyReservationsDetailComponent,
    resolve: {
      myReservations: MyReservationsResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.myReservations.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const myReservationsPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MyReservationsDeletePopupComponent,
    resolve: {
      myReservations: MyReservationsResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.myReservations.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: ':id/reject',
    component: MyReservationsRejectPopupComponent,
    resolve: {
      myReservations: MyReservationsResolve
    },
    data: {
      authorities: ['ROLE_TEACHER'],
      pageTitle: 'srsApp.myReservations.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: ':id/accept',
    component: MyReservationsAcceptPopupComponent,
    resolve: {
      myReservations: MyReservationsResolve
    },
    data: {
      authorities: ['ROLE_TEACHER'],
      pageTitle: 'srsApp.myReservations.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
