import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMajor } from 'app/shared/model/major.model';
import { MajorService } from './major.service';

@Component({
  selector: 'jhi-major-delete-dialog',
  templateUrl: './major-delete-dialog.component.html'
})
export class MajorDeleteDialogComponent {
  major: IMajor;

  constructor(protected majorService: MajorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.majorService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'majorListModification',
        content: 'Deleted an major'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-major-delete-popup',
  template: ''
})
export class MajorDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ major }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MajorDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.major = major;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/major', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/major', { outlets: { popup: null } }]);
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
