import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { ClassHoursDetailComponent } from 'app/entities/class-hours/class-hours-detail.component';
import { ClassHours } from 'app/shared/model/class-hours.model';

describe('Component Tests', () => {
  describe('ClassHours Management Detail Component', () => {
    let comp: ClassHoursDetailComponent;
    let fixture: ComponentFixture<ClassHoursDetailComponent>;
    const route = ({ data: of({ classHours: new ClassHours(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassHoursDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ClassHoursDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassHoursDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classHours).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
