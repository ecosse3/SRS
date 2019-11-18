import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { ClassDurationUpdateComponent } from 'app/entities/class-duration/class-duration-update.component';
import { ClassDurationService } from 'app/entities/class-duration/class-duration.service';
import { ClassDuration } from 'app/shared/model/class-duration.model';

describe('Component Tests', () => {
  describe('ClassDuration Management Update Component', () => {
    let comp: ClassDurationUpdateComponent;
    let fixture: ComponentFixture<ClassDurationUpdateComponent>;
    let service: ClassDurationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassDurationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ClassDurationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassDurationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassDurationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClassDuration(123);
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
        const entity = new ClassDuration();
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
