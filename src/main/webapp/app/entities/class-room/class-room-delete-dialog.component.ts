import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClassRoom } from 'app/shared/model/class-room.model';
import { ClassRoomService } from './class-room.service';

@Component({
  selector: 'jhi-class-room-delete-dialog',
  templateUrl: './class-room-delete-dialog.component.html'
})
export class ClassRoomDeleteDialogComponent {
  classRoom: IClassRoom;

  constructor(protected classRoomService: ClassRoomService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.classRoomService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'classRoomListModification',
        content: 'Deleted an classRoom'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-class-room-delete-popup',
  template: ''
})
export class ClassRoomDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ classRoom }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ClassRoomDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.classRoom = classRoom;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/class-room', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/class-room', { outlets: { popup: null } }]);
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
