import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { ClassHoursDeleteDialogComponent } from 'app/entities/class-hours/class-hours-delete-dialog.component';
import { ClassHoursService } from 'app/entities/class-hours/class-hours.service';

describe('Component Tests', () => {
  describe('ClassHours Management Delete Component', () => {
    let comp: ClassHoursDeleteDialogComponent;
    let fixture: ComponentFixture<ClassHoursDeleteDialogComponent>;
    let service: ClassHoursService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassHoursDeleteDialogComponent]
      })
        .overrideTemplate(ClassHoursDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassHoursDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassHoursService);
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
