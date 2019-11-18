import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { MajorDetailComponent } from 'app/entities/major/major-detail.component';
import { Major } from 'app/shared/model/major.model';

describe('Component Tests', () => {
  describe('Major Management Detail Component', () => {
    let comp: MajorDetailComponent;
    let fixture: ComponentFixture<MajorDetailComponent>;
    const route = ({ data: of({ major: new Major(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [MajorDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MajorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MajorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.major).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
