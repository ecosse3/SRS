import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { SrsTestModule } from '../../../test.module';
import { BuildingDeleteDialogComponent } from 'app/entities/building/building-delete-dialog.component';
import { BuildingService } from 'app/entities/building/building.service';

describe('Component Tests', () => {
  describe('Building Management Delete Component', () => {
    let comp: BuildingDeleteDialogComponent;
    let fixture: ComponentFixture<BuildingDeleteDialogComponent>;
    let service: BuildingService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [BuildingDeleteDialogComponent]
      })
        .overrideTemplate(BuildingDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BuildingDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BuildingService);
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
