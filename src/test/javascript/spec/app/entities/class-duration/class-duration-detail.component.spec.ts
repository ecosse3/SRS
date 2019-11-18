import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { ClassDurationDetailComponent } from 'app/entities/class-duration/class-duration-detail.component';
import { ClassDuration } from 'app/shared/model/class-duration.model';

describe('Component Tests', () => {
  describe('ClassDuration Management Detail Component', () => {
    let comp: ClassDurationDetailComponent;
    let fixture: ComponentFixture<ClassDurationDetailComponent>;
    const route = ({ data: of({ classDuration: new ClassDuration(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassDurationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ClassDurationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassDurationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classDuration).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
