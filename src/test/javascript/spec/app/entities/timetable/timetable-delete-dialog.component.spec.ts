import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { TimetableDeleteDialogComponent } from 'app/entities/timetable/timetable-delete-dialog.component';
import { TimetableService } from 'app/entities/timetable/timetable.service';

describe('Component Tests', () => {
  describe('Timetable Management Delete Component', () => {
    let comp: TimetableDeleteDialogComponent;
    let fixture: ComponentFixture<TimetableDeleteDialogComponent>;
    let service: TimetableService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [TimetableDeleteDialogComponent]
      })
        .overrideTemplate(TimetableDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TimetableDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TimetableService);
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
