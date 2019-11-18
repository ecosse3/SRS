import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SrsTestModule } from '../../../test.module';
import { MajorComponent } from 'app/entities/major/major.component';
import { MajorService } from 'app/entities/major/major.service';
import { Major } from 'app/shared/model/major.model';

describe('Component Tests', () => {
  describe('Major Management Component', () => {
    let comp: MajorComponent;
    let fixture: ComponentFixture<MajorComponent>;
    let service: MajorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [MajorComponent],
        providers: []
      })
        .overrideTemplate(MajorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MajorComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MajorService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Major(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.majors[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
