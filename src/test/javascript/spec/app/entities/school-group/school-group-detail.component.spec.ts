import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { SchoolGroupDetailComponent } from 'app/entities/school-group/school-group-detail.component';
import { SchoolGroup } from 'app/shared/model/school-group.model';

describe('Component Tests', () => {
  describe('SchoolGroup Management Detail Component', () => {
    let comp: SchoolGroupDetailComponent;
    let fixture: ComponentFixture<SchoolGroupDetailComponent>;
    const route = ({ data: of({ schoolGroup: new SchoolGroup(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [SchoolGroupDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SchoolGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SchoolGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.schoolGroup).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
