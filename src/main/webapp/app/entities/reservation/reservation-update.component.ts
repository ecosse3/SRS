import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiLanguageService } from 'ng-jhipster';
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
import { IStatus } from 'app/shared/model/status.model';
import { StatusService } from 'app/entities/status/status.service';
import { TimetableService } from 'app/entities/timetable/timetable.service';
import { ITimetable } from 'app/shared/model/timetable.model';

@Component({
  selector: 'jhi-reservation-update',
  templateUrl: './reservation-update.component.html'
})
export class ReservationUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  schoolgroups: ISchoolGroup[];
  selectedSchoolGroup: any;

  buildings: IBuilding[];
  selectedBuilding: any;

  classrooms: IClassRoom[];
  filteredClassRooms: IClassRoom[];
  selectedClassRoom: any;

  classhours: IClassHours[];

  classdurations: IClassDuration[];

  statuses: IStatus[];

  originalClassDateDp: any;
  newClassDateDp: any;
  minDateToday: any;

  participantsDropdownSettings = {};
  buildingsDropdownSettings = {};
  classRoomsDropdownSettings = {};
  classRoomsDisabledDropdownSettings = {};
  schoolGroupDropdownSettings = {};

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    noteToTeacher: [],
    originalClassDate: [null, [Validators.required]],
    newClassDate: [null, [Validators.required]],
    requestedBy: [],
    createdDate: [],
    participants: [null, [Validators.required]],
    schoolGroup: [null, Validators.required],
    building: [null, Validators.required],
    classRoom: [null, Validators.required],
    originalStartTime: [null, Validators.required],
    newStartTime: [null, Validators.required],
    classDuration: [null, Validators.required],
    status: []
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
    protected statusService: StatusService,
    protected timetableService: TimetableService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected languageService: JhiLanguageService
  ) {}

  ngOnInit() {
    const currentDate = new Date();
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
    this.statusService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IStatus[]>) => mayBeOk.ok),
        map((response: HttpResponse<IStatus[]>) => response.body)
      )
      .subscribe((res: IStatus[]) => (this.statuses = res), (res: HttpErrorResponse) => this.onError(res.message));

    this.participantsDropdownSettings = {
      singleSelection: false,
      text: this.onSelectParticipantsLabel(),
      searchPlaceholderText: this.onSelectSearch(),
      noDataLabel: this.onSelectNoDataLabel(),
      filterSelectAllText: this.onSelectFilterSelectAllParticipantsText(),
      filterUnSelectAllText: this.onSelectFilterUnSelectAllParticipantsText(),
      enableSearchFilter: true,
      enableFilterSelectAll: false,
      enableCheckAll: false,
      badgeShowLimit: 7,
      classes: 'angular2-multiselect'
    };

    this.buildingsDropdownSettings = {
      singleSelection: true,
      showCheckbox: false,
      text: this.onSelectBuildingLabel(),
      searchPlaceholderText: this.onSelectSearch(),
      noDataLabel: this.onSelectNoDataLabel(),
      enableSearchFilter: true,
      enableFilterSelectAll: false,
      enableCheckAll: false,
      classes: 'angular2-multiselect'
    };

    this.classRoomsDropdownSettings = {
      singleSelection: true,
      showCheckbox: false,
      text: this.onSelectClassRoomLabel(),
      searchPlaceholderText: this.onSelectSearch(),
      noDataLabel: this.onSelectNoDataLabel(),
      enableSearchFilter: true,
      enableFilterSelectAll: false,
      enableCheckAll: false,
      classes: 'angular2-multiselect'
    };

    this.classRoomsDisabledDropdownSettings = {
      disabled: true,
      singleSelection: true,
      showCheckbox: false,
      text: this.onSelectDisabledClassRoomLabel(),
      searchPlaceholderText: this.onSelectSearch(),
      noDataLabel: this.onSelectNoDataLabel(),
      enableSearchFilter: true,
      enableFilterSelectAll: false,
      enableCheckAll: false,
      classes: 'angular2-multiselect'
    };

    this.schoolGroupDropdownSettings = {
      singleSelection: true,
      showCheckbox: false,
      text: this.onSelectSchoolGroupLabel(),
      searchPlaceholderText: this.onSelectSearch(),
      noDataLabel: this.onSelectNoDataLabel(),
      enableSearchFilter: true,
      enableFilterSelectAll: false,
      enableCheckAll: false,
      classes: 'angular2-multiselect'
    };

    if (this.editForm.get(['newStartTime']).value === undefined) {
      this.minDateToday = { year: currentDate.getFullYear(), month: currentDate.getMonth() + 1, day: currentDate.getDate() };
    }

    if (this.editForm.get(['building']).value !== undefined || null) {
      this.selectedBuilding = [this.editForm.get(['building']).value];
    }

    if (this.editForm.get(['classRoom']).value !== undefined) {
      this.selectedClassRoom = [this.editForm.get(['classRoom']).value];
    }

    if (this.editForm.get(['schoolGroup']).value !== undefined) {
      this.selectedSchoolGroup = [this.editForm.get(['schoolGroup']).value];
    }

    if (this.editForm.get(['classRoom']).value === null) {
      this.selectedClassRoom = null;
    }
  }

  private getLabel(english: any, polish: any) {
    if (this.languageService.currentLang === 'en') {
      return english;
    }
    return polish;
  }

  onSelectParticipantsLabel() {
    return this.getLabel('Select participants', 'Wybierz uczestników');
  }

  onSelectBuildingLabel() {
    return this.getLabel('Select building', 'Wybierz budynek');
  }

  onSelectClassRoomLabel() {
    return this.getLabel('Select classroom', 'Wybierz salę');
  }

  onSelectSchoolGroupLabel() {
    return this.getLabel('Select school group', 'Wybierz grupę studencką');
  }

  onSelectDisabledClassRoomLabel() {
    return this.getLabel('Select building first', 'Wybierz najpierw budynek');
  }

  onSelectSearch() {
    return this.getLabel('Search...', 'Szukaj...');
  }

  onSelectNoDataLabel() {
    return this.getLabel('Nothing found', 'Nic nie znaleziono');
  }

  onSelectFilterSelectAllParticipantsText() {
    return this.getLabel('Select all filtered results', 'Zaznacz wszystkich pofiltrowanych uczestników');
  }

  onSelectFilterUnSelectAllParticipantsText() {
    return this.getLabel('Unselect all filtered results', 'Odznacz wszystkich pofiltrowanych uczestników');
  }

  updateForm(reservation: IReservation) {
    this.editForm.patchValue({
      id: reservation.id,
      name: reservation.name,
      noteToTeacher: reservation.noteToTeacher,
      originalClassDate: reservation.originalClassDate,
      newClassDate: reservation.newClassDate,
      requestedBy: reservation.requestedBy,
      createdDate: reservation.createdDate != null ? reservation.createdDate.format(DATE_TIME_FORMAT) : null,
      participants: reservation.participants,
      schoolGroup: reservation.schoolGroup,
      building: reservation.building,
      classRoom: reservation.classRoom,
      originalStartTime: reservation.originalStartTime,
      newStartTime: reservation.newStartTime,
      classDuration: reservation.classDuration,
      status: reservation.status
    });

    if (reservation.building) {
      this.getClassRoomByBuildingId(reservation.building);
    }
  }

  previousState() {
    window.history.back();
  }

  save(isOccupied: any) {
    this.isSaving = true;
    const reservation = this.createFromForm();

    if (isOccupied > 0) {
      this.jhiAlertService.addAlert(
        {
          msg: 'srsApp.reservation.occupied',
          params: { date: moment(reservation.newClassDate).format('DD-MM-YYYY'), hour: reservation.newStartTime.startTime },
          type: 'danger',
          timeout: 10000
        },
        []
      );
      this.scrollTop();
      this.isSaving = false;
    } else {
      if (reservation.id !== undefined) {
        this.subscribeToSaveResponse(this.reservationService.update(reservation));
      } else {
        this.subscribeToSaveResponse(this.reservationService.create(reservation));
      }
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
      requestedBy: this.editForm.get(['requestedBy']).value,
      createdDate:
        this.editForm.get(['createdDate']).value != null ? moment(this.editForm.get(['createdDate']).value, DATE_TIME_FORMAT) : undefined,
      participants: this.editForm.get(['participants']).value,
      schoolGroup: this.editForm.get(['schoolGroup']).value[0],
      building: this.editForm.get(['building']).value[0],
      classRoom: this.editForm.get(['classRoom']).value != null ? this.editForm.get(['classRoom']).value[0] : undefined,
      originalStartTime: this.editForm.get(['originalStartTime']).value,
      newStartTime: this.editForm.get(['newStartTime']).value,
      classDuration: this.editForm.get(['classDuration']).value,
      status: this.editForm.get(['status']).value
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

  trackStatusById(index: number, item: IStatus) {
    return item.id;
  }

  getClassRoomByBuildingId(building: any) {
    this.classRoomService.getClassRoomsByBuildingId(building.id).subscribe(res => {
      this.filteredClassRooms = res.body;
    });

    this.selectedClassRoom = [];
  }

  unSelectClassRoom() {
    this.selectedClassRoom = [];
    this.filteredClassRooms = [];
  }

  checkIfClassRoomOccupied(building: any, classRoom: any, newClassDate: any, startTime: any, duration: any) {
    this.timetableService
      .checkIfClassRoomIsOccupied(
        building.id,
        classRoom.id,
        moment(newClassDate)
          .format('YYYY-MM-DD')
          .toString(),
        startTime.id,
        duration.id
      )
      .subscribe(res => {
        this.save(res.body);
      });
  }

  scrollTop() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
  }
}
