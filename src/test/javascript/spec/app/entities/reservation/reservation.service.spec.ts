import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { ReservationService } from 'app/entities/reservation/reservation.service';
import { IReservation, Reservation } from 'app/shared/model/reservation.model';

describe('Service Tests', () => {
  describe('Reservation Service', () => {
    let injector: TestBed;
    let service: ReservationService;
    let httpMock: HttpTestingController;
    let elemDefault: IReservation;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(ReservationService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Reservation(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            originalClassDate: currentDate.format(DATE_FORMAT),
            newClassDate: currentDate.format(DATE_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Reservation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            originalClassDate: currentDate.format(DATE_FORMAT),
            newClassDate: currentDate.format(DATE_FORMAT),
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            originalClassDate: currentDate,
            newClassDate: currentDate,
            createdDate: currentDate
          },
          returnedFromService
        );
        service
          .create(new Reservation(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Reservation', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            noteToTeacher: 'BBBBBB',
            originalClassDate: currentDate.format(DATE_FORMAT),
            newClassDate: currentDate.format(DATE_FORMAT),
            requestedBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            originalClassDate: currentDate,
            newClassDate: currentDate,
            createdDate: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Reservation', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            noteToTeacher: 'BBBBBB',
            originalClassDate: currentDate.format(DATE_FORMAT),
            newClassDate: currentDate.format(DATE_FORMAT),
            requestedBy: 'BBBBBB',
            createdDate: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            originalClassDate: currentDate,
            newClassDate: currentDate,
            createdDate: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Reservation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
