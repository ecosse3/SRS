import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { ClassRoomComponent } from './class-room.component';
import { ClassRoomDetailComponent } from './class-room-detail.component';
import { ClassRoomUpdateComponent } from './class-room-update.component';
import { ClassRoomDeletePopupComponent, ClassRoomDeleteDialogComponent } from './class-room-delete-dialog.component';
import { classRoomRoute, classRoomPopupRoute } from './class-room.route';

const ENTITY_STATES = [...classRoomRoute, ...classRoomPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ClassRoomComponent,
    ClassRoomDetailComponent,
    ClassRoomUpdateComponent,
    ClassRoomDeleteDialogComponent,
    ClassRoomDeletePopupComponent
  ],
  entryComponents: [ClassRoomDeleteDialogComponent]
})
export class SrsClassRoomModule {}
