import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { MyReservationsComponent } from './my-reservations.component';
import { MyReservationsDetailComponent } from './my-reservations-detail.component';
import { MyReservationsDeletePopupComponent, MyReservationsDeleteDialogComponent } from './my-reservations-delete-dialog.component';
import { myReservationsRoute, myReservationsPopupRoute } from './my-reservations.route';

const ENTITY_STATES = [...myReservationsRoute, ...myReservationsPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MyReservationsComponent,
    MyReservationsDetailComponent,
    MyReservationsDeleteDialogComponent,
    MyReservationsDeletePopupComponent
  ],
  entryComponents: [MyReservationsDeleteDialogComponent]
})
export class SrsMyReservationsModule {}
