import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { SchoolGroupUpdateComponent } from 'app/entities/school-group/school-group-update.component';
import { SchoolGroupService } from 'app/entities/school-group/school-group.service';
import { SchoolGroup } from 'app/shared/model/school-group.model';

describe('Component Tests', () => {
  describe('SchoolGroup Management Update Component', () => {
    let comp: SchoolGroupUpdateComponent;
    let fixture: ComponentFixture<SchoolGroupUpdateComponent>;
    let service: SchoolGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [SchoolGroupUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SchoolGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SchoolGroupUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SchoolGroupService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SchoolGroup(123);
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
        const entity = new SchoolGroup();
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
