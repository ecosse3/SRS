import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SrsTestModule } from '../../../test.module';
import { ClassModelComponent } from 'app/entities/class-model/class-model.component';
import { ClassModelService } from 'app/entities/class-model/class-model.service';
import { ClassModel } from 'app/shared/model/class-model.model';

describe('Component Tests', () => {
  describe('ClassModel Management Component', () => {
    let comp: ClassModelComponent;
    let fixture: ComponentFixture<ClassModelComponent>;
    let service: ClassModelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassModelComponent],
        providers: []
      })
        .overrideTemplate(ClassModelComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassModelComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassModelService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ClassModel(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.classModels[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
