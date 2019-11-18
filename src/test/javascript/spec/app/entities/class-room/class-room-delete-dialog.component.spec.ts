import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { ClassRoomDeleteDialogComponent } from 'app/entities/class-room/class-room-delete-dialog.component';
import { ClassRoomService } from 'app/entities/class-room/class-room.service';

describe('Component Tests', () => {
  describe('ClassRoom Management Delete Component', () => {
    let comp: ClassRoomDeleteDialogComponent;
    let fixture: ComponentFixture<ClassRoomDeleteDialogComponent>;
    let service: ClassRoomService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassRoomDeleteDialogComponent]
      })
        .overrideTemplate(ClassRoomDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassRoomDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassRoomService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
