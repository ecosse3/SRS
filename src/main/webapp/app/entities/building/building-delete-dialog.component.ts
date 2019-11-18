import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBuilding } from 'app/shared/model/building.model';
import { BuildingService } from './building.service';

@Component({
  selector: 'jhi-building-delete-dialog',
  templateUrl: './building-delete-dialog.component.html'
})
export class BuildingDeleteDialogComponent {
  building: IBuilding;

  constructor(protected buildingService: BuildingService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.buildingService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'buildingListModification',
        content: 'Deleted an building'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-building-delete-popup',
  template: ''
})
export class BuildingDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ building }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BuildingDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.building = building;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/building', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/building', { outlets: { popup: null } }]);
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
