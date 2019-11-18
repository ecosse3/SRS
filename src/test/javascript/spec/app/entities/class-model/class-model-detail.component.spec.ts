import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { ClassModelDetailComponent } from 'app/entities/class-model/class-model-detail.component';
import { ClassModel } from 'app/shared/model/class-model.model';

describe('Component Tests', () => {
  describe('ClassModel Management Detail Component', () => {
    let comp: ClassModelDetailComponent;
    let fixture: ComponentFixture<ClassModelDetailComponent>;
    const route = ({ data: of({ classModel: new ClassModel(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassModelDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ClassModelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassModelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classModel).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
