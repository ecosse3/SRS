import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { TimetableComponent } from './timetable.component';
import { TimetableDetailComponent } from './timetable-detail.component';
import { TimetableUpdateComponent } from './timetable-update.component';
import { TimetableDeletePopupComponent, TimetableDeleteDialogComponent } from './timetable-delete-dialog.component';
import { timetableRoute, timetablePopupRoute } from './timetable.route';

const ENTITY_STATES = [...timetableRoute, ...timetablePopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TimetableComponent,
    TimetableDetailComponent,
    TimetableUpdateComponent,
    TimetableDeleteDialogComponent,
    TimetableDeletePopupComponent
  ],
  entryComponents: [TimetableDeleteDialogComponent]
})
export class SrsTimetableModule {}
