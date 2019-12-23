import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SrsTestModule } from '../../../test.module';
import { MyReservationsComponent } from 'app/entities/my-reservations/my-reservations.component';
import { MyReservationsService } from 'app/entities/my-reservations/my-reservations.service';
import { MyReservations } from 'app/shared/model/my-reservations.model';

describe('Component Tests', () => {
  describe('MyReservations Management Component', () => {
    let comp: MyReservationsComponent;
    let fixture: ComponentFixture<MyReservationsComponent>;
    let service: MyReservationsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [MyReservationsComponent],
        providers: []
      })
        .overrideTemplate(MyReservationsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MyReservationsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MyReservationsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new MyReservations(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.myReservations[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
