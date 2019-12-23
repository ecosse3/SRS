import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ClassDuration } from 'app/shared/model/class-duration.model';
import { ClassDurationService } from './class-duration.service';
import { ClassDurationComponent } from './class-duration.component';
import { ClassDurationDetailComponent } from './class-duration-detail.component';
import { ClassDurationUpdateComponent } from './class-duration-update.component';
import { ClassDurationDeletePopupComponent } from './class-duration-delete-dialog.component';
import { IClassDuration } from 'app/shared/model/class-duration.model';

@Injectable({ providedIn: 'root' })
export class ClassDurationResolve implements Resolve<IClassDuration> {
  constructor(private service: ClassDurationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IClassDuration> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ClassDuration>) => response.ok),
        map((classDuration: HttpResponse<ClassDuration>) => classDuration.body)
      );
    }
    return of(new ClassDuration());
  }
}

export const classDurationRoute: Routes = [
  {
    path: '',
    component: ClassDurationComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classDuration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClassDurationDetailComponent,
    resolve: {
      classDuration: ClassDurationResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classDuration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClassDurationUpdateComponent,
    resolve: {
      classDuration: ClassDurationResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classDuration.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClassDurationUpdateComponent,
    resolve: {
      classDuration: ClassDurationResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classDuration.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const classDurationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ClassDurationDeletePopupComponent,
    resolve: {
      classDuration: ClassDurationResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classDuration.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
