import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { ClassDurationComponent } from './class-duration.component';
import { ClassDurationDetailComponent } from './class-duration-detail.component';
import { ClassDurationUpdateComponent } from './class-duration-update.component';
import { ClassDurationDeletePopupComponent, ClassDurationDeleteDialogComponent } from './class-duration-delete-dialog.component';
import { classDurationRoute, classDurationPopupRoute } from './class-duration.route';

const ENTITY_STATES = [...classDurationRoute, ...classDurationPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ClassDurationComponent,
    ClassDurationDetailComponent,
    ClassDurationUpdateComponent,
    ClassDurationDeleteDialogComponent,
    ClassDurationDeletePopupComponent
  ],
  entryComponents: [ClassDurationDeleteDialogComponent]
})
export class SrsClassDurationModule {}
