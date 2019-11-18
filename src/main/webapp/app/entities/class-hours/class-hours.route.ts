import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ClassHours } from 'app/shared/model/class-hours.model';
import { ClassHoursService } from './class-hours.service';
import { ClassHoursComponent } from './class-hours.component';
import { ClassHoursDetailComponent } from './class-hours-detail.component';
import { ClassHoursUpdateComponent } from './class-hours-update.component';
import { ClassHoursDeletePopupComponent } from './class-hours-delete-dialog.component';
import { IClassHours } from 'app/shared/model/class-hours.model';

@Injectable({ providedIn: 'root' })
export class ClassHoursResolve implements Resolve<IClassHours> {
  constructor(private service: ClassHoursService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IClassHours> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ClassHours>) => response.ok),
        map((classHours: HttpResponse<ClassHours>) => classHours.body)
      );
    }
    return of(new ClassHours());
  }
}

export const classHoursRoute: Routes = [
  {
    path: '',
    component: ClassHoursComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.classHours.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClassHoursDetailComponent,
    resolve: {
      classHours: ClassHoursResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.classHours.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClassHoursUpdateComponent,
    resolve: {
      classHours: ClassHoursResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.classHours.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClassHoursUpdateComponent,
    resolve: {
      classHours: ClassHoursResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.classHours.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const classHoursPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ClassHoursDeletePopupComponent,
    resolve: {
      classHours: ClassHoursResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.classHours.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
