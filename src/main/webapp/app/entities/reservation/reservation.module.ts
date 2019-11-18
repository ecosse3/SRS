import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { ReservationComponent } from './reservation.component';
import { ReservationDetailComponent } from './reservation-detail.component';
import { ReservationUpdateComponent } from './reservation-update.component';
import { ReservationDeletePopupComponent, ReservationDeleteDialogComponent } from './reservation-delete-dialog.component';
import { reservationRoute, reservationPopupRoute } from './reservation.route';

const ENTITY_STATES = [...reservationRoute, ...reservationPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ReservationComponent,
    ReservationDetailComponent,
    ReservationUpdateComponent,
    ReservationDeleteDialogComponent,
    ReservationDeletePopupComponent
  ],
  entryComponents: [ReservationDeleteDialogComponent]
})
export class SrsReservationModule {}
