import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SrsTestModule } from '../../../test.module';
import { ClassHoursComponent } from 'app/entities/class-hours/class-hours.component';
import { ClassHoursService } from 'app/entities/class-hours/class-hours.service';
import { ClassHours } from 'app/shared/model/class-hours.model';

describe('Component Tests', () => {
  describe('ClassHours Management Component', () => {
    let comp: ClassHoursComponent;
    let fixture: ComponentFixture<ClassHoursComponent>;
    let service: ClassHoursService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassHoursComponent],
        providers: []
      })
        .overrideTemplate(ClassHoursComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassHoursComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassHoursService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ClassHours(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.classHours[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
