import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { StatusDetailComponent } from 'app/entities/status/status-detail.component';
import { Status } from 'app/shared/model/status.model';

describe('Component Tests', () => {
  describe('Status Management Detail Component', () => {
    let comp: StatusDetailComponent;
    let fixture: ComponentFixture<StatusDetailComponent>;
    const route = ({ data: of({ status: new Status(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [StatusDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(StatusDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(StatusDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.status).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
