import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { ClassModelDeleteDialogComponent } from 'app/entities/class-model/class-model-delete-dialog.component';
import { ClassModelService } from 'app/entities/class-model/class-model.service';

describe('Component Tests', () => {
  describe('ClassModel Management Delete Component', () => {
    let comp: ClassModelDeleteDialogComponent;
    let fixture: ComponentFixture<ClassModelDeleteDialogComponent>;
    let service: ClassModelService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassModelDeleteDialogComponent]
      })
        .overrideTemplate(ClassModelDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassModelDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassModelService);
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
