import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { MajorUpdateComponent } from 'app/entities/major/major-update.component';
import { MajorService } from 'app/entities/major/major.service';
import { Major } from 'app/shared/model/major.model';

describe('Component Tests', () => {
  describe('Major Management Update Component', () => {
    let comp: MajorUpdateComponent;
    let fixture: ComponentFixture<MajorUpdateComponent>;
    let service: MajorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [MajorUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MajorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MajorUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MajorService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Major(123);
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
        const entity = new Major();
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
