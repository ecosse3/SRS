import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { ClassHoursComponent } from './class-hours.component';
import { ClassHoursDetailComponent } from './class-hours-detail.component';
import { ClassHoursUpdateComponent } from './class-hours-update.component';
import { ClassHoursDeletePopupComponent, ClassHoursDeleteDialogComponent } from './class-hours-delete-dialog.component';
import { classHoursRoute, classHoursPopupRoute } from './class-hours.route';

const ENTITY_STATES = [...classHoursRoute, ...classHoursPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ClassHoursComponent,
    ClassHoursDetailComponent,
    ClassHoursUpdateComponent,
    ClassHoursDeleteDialogComponent,
    ClassHoursDeletePopupComponent
  ],
  entryComponents: [ClassHoursDeleteDialogComponent]
})
export class SrsClassHoursModule {}
