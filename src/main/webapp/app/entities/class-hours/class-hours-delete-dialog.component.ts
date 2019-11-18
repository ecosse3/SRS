import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClassHours } from 'app/shared/model/class-hours.model';
import { ClassHoursService } from './class-hours.service';

@Component({
  selector: 'jhi-class-hours-delete-dialog',
  templateUrl: './class-hours-delete-dialog.component.html'
})
export class ClassHoursDeleteDialogComponent {
  classHours: IClassHours;

  constructor(
    protected classHoursService: ClassHoursService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.classHoursService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'classHoursListModification',
        content: 'Deleted an classHours'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-class-hours-delete-popup',
  template: ''
})
export class ClassHoursDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ classHours }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ClassHoursDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.classHours = classHours;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/class-hours', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/class-hours', { outlets: { popup: null } }]);
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
