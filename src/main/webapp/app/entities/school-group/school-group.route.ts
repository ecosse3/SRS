import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SchoolGroup } from 'app/shared/model/school-group.model';
import { SchoolGroupService } from './school-group.service';
import { SchoolGroupComponent } from './school-group.component';
import { SchoolGroupDetailComponent } from './school-group-detail.component';
import { SchoolGroupUpdateComponent } from './school-group-update.component';
import { SchoolGroupDeletePopupComponent } from './school-group-delete-dialog.component';
import { ISchoolGroup } from 'app/shared/model/school-group.model';

@Injectable({ providedIn: 'root' })
export class SchoolGroupResolve implements Resolve<ISchoolGroup> {
  constructor(private service: SchoolGroupService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISchoolGroup> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<SchoolGroup>) => response.ok),
        map((schoolGroup: HttpResponse<SchoolGroup>) => schoolGroup.body)
      );
    }
    return of(new SchoolGroup());
  }
}

export const schoolGroupRoute: Routes = [
  {
    path: '',
    component: SchoolGroupComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'srsApp.schoolGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SchoolGroupDetailComponent,
    resolve: {
      schoolGroup: SchoolGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.schoolGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SchoolGroupUpdateComponent,
    resolve: {
      schoolGroup: SchoolGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.schoolGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SchoolGroupUpdateComponent,
    resolve: {
      schoolGroup: SchoolGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.schoolGroup.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const schoolGroupPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SchoolGroupDeletePopupComponent,
    resolve: {
      schoolGroup: SchoolGroupResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'srsApp.schoolGroup.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
