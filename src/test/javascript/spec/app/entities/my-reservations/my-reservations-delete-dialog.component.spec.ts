import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { MyReservationsDeleteDialogComponent } from 'app/entities/my-reservations/my-reservations-delete-dialog.component';
import { MyReservationsService } from 'app/entities/my-reservations/my-reservations.service';

describe('Component Tests', () => {
  describe('MyReservations Management Delete Component', () => {
    let comp: MyReservationsDeleteDialogComponent;
    let fixture: ComponentFixture<MyReservationsDeleteDialogComponent>;
    let service: MyReservationsService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [MyReservationsDeleteDialogComponent]
      })
        .overrideTemplate(MyReservationsDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MyReservationsDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MyReservationsService);
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
