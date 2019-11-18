import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { SchoolGroupComponent } from './school-group.component';
import { SchoolGroupDetailComponent } from './school-group-detail.component';
import { SchoolGroupUpdateComponent } from './school-group-update.component';
import { SchoolGroupDeletePopupComponent, SchoolGroupDeleteDialogComponent } from './school-group-delete-dialog.component';
import { schoolGroupRoute, schoolGroupPopupRoute } from './school-group.route';

const ENTITY_STATES = [...schoolGroupRoute, ...schoolGroupPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    SchoolGroupComponent,
    SchoolGroupDetailComponent,
    SchoolGroupUpdateComponent,
    SchoolGroupDeleteDialogComponent,
    SchoolGroupDeletePopupComponent
  ],
  entryComponents: [SchoolGroupDeleteDialogComponent]
})
export class SrsSchoolGroupModule {}
