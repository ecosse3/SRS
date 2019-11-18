import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { MajorComponent } from './major.component';
import { MajorDetailComponent } from './major-detail.component';
import { MajorUpdateComponent } from './major-update.component';
import { MajorDeletePopupComponent, MajorDeleteDialogComponent } from './major-delete-dialog.component';
import { majorRoute, majorPopupRoute } from './major.route';

const ENTITY_STATES = [...majorRoute, ...majorPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [MajorComponent, MajorDetailComponent, MajorUpdateComponent, MajorDeleteDialogComponent, MajorDeletePopupComponent],
  entryComponents: [MajorDeleteDialogComponent]
})
export class SrsMajorModule {}
