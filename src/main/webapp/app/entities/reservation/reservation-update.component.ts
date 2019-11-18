import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IReservation, Reservation } from 'app/shared/model/reservation.model';
import { ReservationService } from './reservation.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { ISchoolGroup } from 'app/shared/model/school-group.model';
import { SchoolGroupService } from 'app/entities/school-group/school-group.service';
import { IBuilding } from 'app/shared/model/building.model';
import { BuildingService } from 'app/entities/building/building.service';
import { IClassRoom } from 'app/shared/model/class-room.model';
import { ClassRoomService } from 'app/entities/class-room/class-room.service';
import { IClassHours } from 'app/shared/model/class-hours.model';
import { ClassHoursService } from 'app/entities/class-hours/class-hours.service';
import { IClassDuration } from 'app/shared/model/class-duration.model';
import { ClassDurationService } from 'app/entities/class-duration/class-duration.service';

@Component({
  selector: 'jhi-reservation-update',
  templateUrl: './reservation-update.component.html'
})
export class ReservationUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  schoolgroups: ISchoolGroup[];

  buildings: IBuilding[];

  classrooms: IClassRoom[];

  classhours: IClassHours[];

  classdurations: IClassDuration[];
  originalClassDateDp: any;
  newClassDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    noteToTeacher: [],
    originalClassDate: [null, [Validators.required]],
    newClassDate: [null, [Validators.required]],
    participants: [],
    schoolGroup: [null, Validators.required],
    building: [null, Validators.required],
    classRoom: [],
    originalStartTime: [null, Validators.required],
    newStartTime: [],
    classDuration: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected reservationService: ReservationService,
    protected userService: UserService,
    protected schoolGroupService: SchoolGroupService,
    protected buildingService: BuildingService,
    protected classRoomService: ClassRoomService,
    protected classHoursService: ClassHoursService,
    protected classDurationService: ClassDurationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ reservation }) => {
      this.updateForm(reservation);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.schoolGroupService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISchoolGroup[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISchoolGroup[]>) => response.body)
      )
      .subscribe((res: ISchoolGroup[]) => (this.schoolgroups = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.buildingService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBuilding[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBuilding[]>) => response.body)
      )
      .subscribe((res: IBuilding[]) => (this.buildings = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.classRoomService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClassRoom[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClassRoom[]>) => response.body)
      )
      .subscribe((res: IClassRoom[]) => (this.classrooms = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.classHoursService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClassHours[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClassHours[]>) => response.body)
      )
      .subscribe((res: IClassHours[]) => (this.classhours = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.classDurationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClassDuration[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClassDuration[]>) => response.body)
      )
      .subscribe((res: IClassDuration[]) => (this.classdurations = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(reservation: IReservation) {
    this.editForm.patchValue({
      id: reservation.id,
      name: reservation.name,
      noteToTeacher: reservation.noteToTeacher,
      originalClassDate: reservation.originalClassDate,
      newClassDate: reservation.newClassDate,
      participants: reservation.participants,
      schoolGroup: reservation.schoolGroup,
      building: reservation.building,
      classRoom: reservation.classRoom,
      originalStartTime: reservation.originalStartTime,
      newStartTime: reservation.newStartTime,
      classDuration: reservation.classDuration
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const reservation = this.createFromForm();
    if (reservation.id !== undefined) {
      this.subscribeToSaveResponse(this.reservationService.update(reservation));
    } else {
      this.subscribeToSaveResponse(this.reservationService.create(reservation));
    }
  }

  private createFromForm(): IReservation {
    return {
      ...new Reservation(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      noteToTeacher: this.editForm.get(['noteToTeacher']).value,
      originalClassDate: this.editForm.get(['originalClassDate']).value,
      newClassDate: this.editForm.get(['newClassDate']).value,
      participants: this.editForm.get(['participants']).value,
      schoolGroup: this.editForm.get(['schoolGroup']).value,
      building: this.editForm.get(['building']).value,
      classRoom: this.editForm.get(['classRoom']).value,
      originalStartTime: this.editForm.get(['originalStartTime']).value,
      newStartTime: this.editForm.get(['newStartTime']).value,
      classDuration: this.editForm.get(['classDuration']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReservation>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackSchoolGroupById(index: number, item: ISchoolGroup) {
    return item.id;
  }

  trackBuildingById(index: number, item: IBuilding) {
    return item.id;
  }

  trackClassRoomById(index: number, item: IClassRoom) {
    return item.id;
  }

  trackClassHoursById(index: number, item: IClassHours) {
    return item.id;
  }

  trackClassDurationById(index: number, item: IClassDuration) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
