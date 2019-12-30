import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITimetable } from 'app/shared/model/timetable.model';
import { TimetableService } from './timetable.service';

@Component({
  selector: 'jhi-timetable-delete-dialog',
  templateUrl: './timetable-delete-dialog.component.html'
})
export class TimetableDeleteDialogComponent {
  timetable: ITimetable;

  constructor(protected timetableService: TimetableService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.timetableService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'timetableListModification',
        content: 'Deleted an timetable'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-timetable-delete-popup',
  template: ''
})
export class TimetableDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ timetable }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TimetableDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.timetable = timetable;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/timetable', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/timetable', { outlets: { popup: null } }]);
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
