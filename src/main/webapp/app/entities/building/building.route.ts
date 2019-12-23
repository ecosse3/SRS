import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Building } from 'app/shared/model/building.model';
import { BuildingService } from './building.service';
import { BuildingComponent } from './building.component';
import { BuildingDetailComponent } from './building-detail.component';
import { BuildingUpdateComponent } from './building-update.component';
import { BuildingDeletePopupComponent } from './building-delete-dialog.component';
import { IBuilding } from 'app/shared/model/building.model';

@Injectable({ providedIn: 'root' })
export class BuildingResolve implements Resolve<IBuilding> {
  constructor(private service: BuildingService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBuilding> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Building>) => response.ok),
        map((building: HttpResponse<Building>) => building.body)
      );
    }
    return of(new Building());
  }
}

export const buildingRoute: Routes = [
  {
    path: '',
    component: BuildingComponent,
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BuildingDetailComponent,
    resolve: {
      building: BuildingResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BuildingUpdateComponent,
    resolve: {
      building: BuildingResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BuildingUpdateComponent,
    resolve: {
      building: BuildingResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.building.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const buildingPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BuildingDeletePopupComponent,
    resolve: {
      building: BuildingResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'srsApp.building.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
