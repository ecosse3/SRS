import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMyReservations } from 'app/shared/model/my-reservations.model';
import { MyReservationsService } from './my-reservations.service';
import { IReservation } from 'app/shared/model/reservation.model';
import { HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'jhi-my-reservations-reject-dialog',
  templateUrl: './my-reservations-reject-dialog.component.html'
})
export class MyReservationsRejectDialogComponent {
  myReservations: IMyReservations;

  constructor(
    protected myReservationsService: MyReservationsService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmReject(myReservation: IReservation) {
    this.myReservationsService.reject(myReservation).subscribe(response => {
      this.eventManager.broadcast({
        name: 'myReservationsListModification',
        content: 'Rejected an myReservations'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-my-reservations-reject-popup',
  template: ''
})
export class MyReservationsRejectPopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ myReservations }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MyReservationsRejectDialogComponent as Component, { size: 'lg', backdrop: 'static' });
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
