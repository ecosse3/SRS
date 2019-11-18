import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISchoolGroup } from 'app/shared/model/school-group.model';
import { SchoolGroupService } from './school-group.service';

@Component({
  selector: 'jhi-school-group-delete-dialog',
  templateUrl: './school-group-delete-dialog.component.html'
})
export class SchoolGroupDeleteDialogComponent {
  schoolGroup: ISchoolGroup;

  constructor(
    protected schoolGroupService: SchoolGroupService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.schoolGroupService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'schoolGroupListModification',
        content: 'Deleted an schoolGroup'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-school-group-delete-popup',
  template: ''
})
export class SchoolGroupDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ schoolGroup }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(SchoolGroupDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.schoolGroup = schoolGroup;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/school-group', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/school-group', { outlets: { popup: null } }]);
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
