import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClassModel } from 'app/shared/model/class-model.model';
import { ClassModelService } from './class-model.service';

@Component({
  selector: 'jhi-class-model-delete-dialog',
  templateUrl: './class-model-delete-dialog.component.html'
})
export class ClassModelDeleteDialogComponent {
  classModel: IClassModel;

  constructor(
    protected classModelService: ClassModelService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.classModelService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'classModelListModification',
        content: 'Deleted an classModel'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-class-model-delete-popup',
  template: ''
})
export class ClassModelDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ classModel }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ClassModelDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.classModel = classModel;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/class-model', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/class-model', { outlets: { popup: null } }]);
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
