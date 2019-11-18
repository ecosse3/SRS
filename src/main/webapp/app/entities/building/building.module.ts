import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { BuildingComponent } from './building.component';
import { BuildingDetailComponent } from './building-detail.component';
import { BuildingUpdateComponent } from './building-update.component';
import { BuildingDeletePopupComponent, BuildingDeleteDialogComponent } from './building-delete-dialog.component';
import { buildingRoute, buildingPopupRoute } from './building.route';

const ENTITY_STATES = [...buildingRoute, ...buildingPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BuildingComponent,
    BuildingDetailComponent,
    BuildingUpdateComponent,
    BuildingDeleteDialogComponent,
    BuildingDeletePopupComponent
  ],
  entryComponents: [BuildingDeleteDialogComponent]
})
export class SrsBuildingModule {}
