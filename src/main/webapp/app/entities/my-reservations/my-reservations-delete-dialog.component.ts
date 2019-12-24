import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMyReservations } from 'app/shared/model/my-reservations.model';
import { MyReservationsService } from './my-reservations.service';
import { IReservation } from 'app/shared/model/reservation.model';

@Component({
  selector: 'jhi-my-reservations-delete-dialog',
  templateUrl: './my-reservations-delete-dialog.component.html'
})
export class MyReservationsDeleteDialogComponent {
  myReservations: IMyReservations;

  constructor(
    protected myReservationsService: MyReservationsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmCancel(myReservation: IReservation) {
    this.myReservationsService.cancel(myReservation).subscribe(response => {
      this.eventManager.broadcast({
        name: 'myReservationsListModification',
        content: 'Canceled an myReservations'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-my-reservations-delete-popup',
  template: ''
})
export class MyReservationsDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ myReservations }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MyReservationsDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.myReservations = myReservations;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/my-reservations', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/my-reservations', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
