import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { SchoolGroupDeleteDialogComponent } from 'app/entities/school-group/school-group-delete-dialog.component';
import { SchoolGroupService } from 'app/entities/school-group/school-group.service';

describe('Component Tests', () => {
  describe('SchoolGroup Management Delete Component', () => {
    let comp: SchoolGroupDeleteDialogComponent;
    let fixture: ComponentFixture<SchoolGroupDeleteDialogComponent>;
    let service: SchoolGroupService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [SchoolGroupDeleteDialogComponent]
      })
        .overrideTemplate(SchoolGroupDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolGroupDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SchoolGroupService);
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
