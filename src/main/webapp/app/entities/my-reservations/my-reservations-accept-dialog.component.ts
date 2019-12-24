import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMyReservations } from 'app/shared/model/my-reservations.model';
import { MyReservationsService } from './my-reservations.service';
import { IReservation } from 'app/shared/model/reservation.model';

@Component({
  selector: 'jhi-my-reservations-accept-dialog',
  templateUrl: './my-reservations-accept-dialog.component.html'
})
export class MyReservationsAcceptDialogComponent {
  myReservations: IMyReservations;

  constructor(
    protected myReservationsService: MyReservationsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmAccept(myReservation: IReservation) {
    this.myReservationsService.accept(myReservation).subscribe(response => {
      this.eventManager.broadcast({
        name: 'myReservationsListModification',
        content: 'Accepted an myReservations'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-my-reservations-accept-popup',
  template: ''
})
export class MyReservationsAcceptPopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ myReservations }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MyReservationsAcceptDialogComponent as Component, { size: 'lg', backdrop: 'static' });
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
