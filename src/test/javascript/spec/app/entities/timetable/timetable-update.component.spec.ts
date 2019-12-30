import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { TimetableUpdateComponent } from 'app/entities/timetable/timetable-update.component';
import { TimetableService } from 'app/entities/timetable/timetable.service';
import { Timetable } from 'app/shared/model/timetable.model';

describe('Component Tests', () => {
  describe('Timetable Management Update Component', () => {
    let comp: TimetableUpdateComponent;
    let fixture: ComponentFixture<TimetableUpdateComponent>;
    let service: TimetableService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [TimetableUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TimetableUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TimetableUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TimetableService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Timetable(123);
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
        const entity = new Timetable();
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
