import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { ClassModel } from 'app/shared/model/class-model.model';
import { ClassModelService } from './class-model.service';
import { ClassModelComponent } from './class-model.component';
import { ClassModelDetailComponent } from './class-model-detail.component';
import { ClassModelUpdateComponent } from './class-model-update.component';
import { ClassModelDeletePopupComponent } from './class-model-delete-dialog.component';
import { IClassModel } from 'app/shared/model/class-model.model';

@Injectable({ providedIn: 'root' })
export class ClassModelResolve implements Resolve<IClassModel> {
  constructor(private service: ClassModelService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IClassModel> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<ClassModel>) => response.ok),
        map((classModel: HttpResponse<ClassModel>) => classModel.body)
      );
    }
    return of(new ClassModel());
  }
}

export const classModelRoute: Routes = [
  {
    path: '',
    component: ClassModelComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classModel.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClassModelDetailComponent,
    resolve: {
      classModel: ClassModelResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classModel.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClassModelUpdateComponent,
    resolve: {
      classModel: ClassModelResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classModel.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClassModelUpdateComponent,
    resolve: {
      classModel: ClassModelResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classModel.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const classModelPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ClassModelDeletePopupComponent,
    resolve: {
      classModel: ClassModelResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.classModel.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
