import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { TimetableDetailComponent } from 'app/entities/timetable/timetable-detail.component';
import { Timetable } from 'app/shared/model/timetable.model';

describe('Component Tests', () => {
  describe('Timetable Management Detail Component', () => {
    let comp: TimetableDetailComponent;
    let fixture: ComponentFixture<TimetableDetailComponent>;
    const route = ({ data: of({ timetable: new Timetable(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [TimetableDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TimetableDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TimetableDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.timetable).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
