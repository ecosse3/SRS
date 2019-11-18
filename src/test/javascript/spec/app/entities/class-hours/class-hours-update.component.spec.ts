import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { ClassHoursUpdateComponent } from 'app/entities/class-hours/class-hours-update.component';
import { ClassHoursService } from 'app/entities/class-hours/class-hours.service';
import { ClassHours } from 'app/shared/model/class-hours.model';

describe('Component Tests', () => {
  describe('ClassHours Management Update Component', () => {
    let comp: ClassHoursUpdateComponent;
    let fixture: ComponentFixture<ClassHoursUpdateComponent>;
    let service: ClassHoursService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassHoursUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ClassHoursUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassHoursUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassHoursService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClassHours(123);
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
        const entity = new ClassHours();
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
