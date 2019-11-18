import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SrsTestModule } from '../../../test.module';
import { ClassDurationComponent } from 'app/entities/class-duration/class-duration.component';
import { ClassDurationService } from 'app/entities/class-duration/class-duration.service';
import { ClassDuration } from 'app/shared/model/class-duration.model';

describe('Component Tests', () => {
  describe('ClassDuration Management Component', () => {
    let comp: ClassDurationComponent;
    let fixture: ComponentFixture<ClassDurationComponent>;
    let service: ClassDurationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassDurationComponent],
        providers: []
      })
        .overrideTemplate(ClassDurationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassDurationComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassDurationService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ClassDuration(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.classDurations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
