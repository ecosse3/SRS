import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { ClassModelUpdateComponent } from 'app/entities/class-model/class-model-update.component';
import { ClassModelService } from 'app/entities/class-model/class-model.service';
import { ClassModel } from 'app/shared/model/class-model.model';

describe('Component Tests', () => {
  describe('ClassModel Management Update Component', () => {
    let comp: ClassModelUpdateComponent;
    let fixture: ComponentFixture<ClassModelUpdateComponent>;
    let service: ClassModelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassModelUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ClassModelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassModelUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassModelService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClassModel(123);
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
        const entity = new ClassModel();
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
