import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Major } from 'app/shared/model/major.model';
import { MajorService } from './major.service';
import { MajorComponent } from './major.component';
import { MajorDetailComponent } from './major-detail.component';
import { MajorUpdateComponent } from './major-update.component';
import { MajorDeletePopupComponent } from './major-delete-dialog.component';
import { IMajor } from 'app/shared/model/major.model';

@Injectable({ providedIn: 'root' })
export class MajorResolve implements Resolve<IMajor> {
  constructor(private service: MajorService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMajor> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Major>) => response.ok),
        map((major: HttpResponse<Major>) => major.body)
      );
    }
    return of(new Major());
  }
}

export const majorRoute: Routes = [
  {
    path: '',
    component: MajorComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.major.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MajorDetailComponent,
    resolve: {
      major: MajorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.major.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MajorUpdateComponent,
    resolve: {
      major: MajorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.major.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MajorUpdateComponent,
    resolve: {
      major: MajorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.major.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const majorPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MajorDeletePopupComponent,
    resolve: {
      major: MajorResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.major.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
