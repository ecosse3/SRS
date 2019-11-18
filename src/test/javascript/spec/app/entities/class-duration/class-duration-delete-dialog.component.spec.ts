import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { ClassDurationDeleteDialogComponent } from 'app/entities/class-duration/class-duration-delete-dialog.component';
import { ClassDurationService } from 'app/entities/class-duration/class-duration.service';

describe('Component Tests', () => {
  describe('ClassDuration Management Delete Component', () => {
    let comp: ClassDurationDeleteDialogComponent;
    let fixture: ComponentFixture<ClassDurationDeleteDialogComponent>;
    let service: ClassDurationService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassDurationDeleteDialogComponent]
      })
        .overrideTemplate(ClassDurationDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassDurationDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassDurationService);
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
