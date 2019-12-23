import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { MyReservationsUpdateComponent } from 'app/entities/my-reservations/my-reservations-update.component';
import { MyReservationsService } from 'app/entities/my-reservations/my-reservations.service';
import { MyReservations } from 'app/shared/model/my-reservations.model';

describe('Component Tests', () => {
  describe('MyReservations Management Update Component', () => {
    let comp: MyReservationsUpdateComponent;
    let fixture: ComponentFixture<MyReservationsUpdateComponent>;
    let service: MyReservationsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [MyReservationsUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MyReservationsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MyReservationsUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MyReservationsService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MyReservations(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MyReservations();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
