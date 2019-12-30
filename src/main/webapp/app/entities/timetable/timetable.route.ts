import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Timetable } from 'app/shared/model/timetable.model';
import { TimetableService } from './timetable.service';
import { TimetableComponent } from './timetable.component';
import { TimetableDetailComponent } from './timetable-detail.component';
import { TimetableUpdateComponent } from './timetable-update.component';
import { TimetableDeletePopupComponent } from './timetable-delete-dialog.component';
import { ITimetable } from 'app/shared/model/timetable.model';

@Injectable({ providedIn: 'root' })
export class TimetableResolve implements Resolve<ITimetable> {
  constructor(private service: TimetableService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITimetable> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Timetable>) => response.ok),
        map((timetable: HttpResponse<Timetable>) => timetable.body)
      );
    }
    return of(new Timetable());
  }
}

export const timetableRoute: Routes = [
  {
    path: '',
    component: TimetableComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'srsApp.timetable.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TimetableDetailComponent,
    resolve: {
      timetable: TimetableResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.timetable.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TimetableUpdateComponent,
    resolve: {
      timetable: TimetableResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.timetable.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TimetableUpdateComponent,
    resolve: {
      timetable: TimetableResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.timetable.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const timetablePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TimetableDeletePopupComponent,
    resolve: {
      timetable: TimetableResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.timetable.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
