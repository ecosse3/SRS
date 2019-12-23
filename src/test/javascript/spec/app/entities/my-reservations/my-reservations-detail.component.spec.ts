import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { MyReservationsDetailComponent } from 'app/entities/my-reservations/my-reservations-detail.component';
import { MyReservations } from 'app/shared/model/my-reservations.model';

describe('Component Tests', () => {
  describe('MyReservations Management Detail Component', () => {
    let comp: MyReservationsDetailComponent;
    let fixture: ComponentFixture<MyReservationsDetailComponent>;
    const route = ({ data: of({ myReservations: new MyReservations(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [MyReservationsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MyReservationsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MyReservationsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.myReservations).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
