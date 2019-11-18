import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClassDuration } from 'app/shared/model/class-duration.model';
import { ClassDurationService } from './class-duration.service';

@Component({
  selector: 'jhi-class-duration-delete-dialog',
  templateUrl: './class-duration-delete-dialog.component.html'
})
export class ClassDurationDeleteDialogComponent {
  classDuration: IClassDuration;

  constructor(
    protected classDurationService: ClassDurationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.classDurationService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'classDurationListModification',
        content: 'Deleted an classDuration'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-class-duration-delete-popup',
  template: ''
})
export class ClassDurationDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ classDuration }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ClassDurationDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.classDuration = classDuration;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/class-duration', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/class-duration', { outlets: { popup: null } }]);
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
