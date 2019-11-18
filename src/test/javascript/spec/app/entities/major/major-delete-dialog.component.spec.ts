import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { MajorDeleteDialogComponent } from 'app/entities/major/major-delete-dialog.component';
import { MajorService } from 'app/entities/major/major.service';

describe('Component Tests', () => {
  describe('Major Management Delete Component', () => {
    let comp: MajorDeleteDialogComponent;
    let fixture: ComponentFixture<MajorDeleteDialogComponent>;
    let service: MajorService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [MajorDeleteDialogComponent]
      })
        .overrideTemplate(MajorDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MajorDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MajorService);
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
