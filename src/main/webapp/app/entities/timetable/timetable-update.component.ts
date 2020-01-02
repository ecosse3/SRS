import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService, JhiLanguageService } from 'ng-jhipster';
import { ITimetable, Timetable } from 'app/shared/model/timetable.model';
import { TimetableService } from './timetable.service';
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
  selector: 'jhi-timetable-update',
  templateUrl: './timetable-update.component.html'
})
export class TimetableUpdateComponent implements OnInit {
  isSaving: boolean;

  schoolgroups: ISchoolGroup[];

  buildings: IBuilding[];
  selectedBuilding: any;

  classrooms: IClassRoom[];
  filteredClassRooms: IClassRoom[];
  selectedClassRoom: any;

  classhours: IClassHours[];

  classdurations: IClassDuration[];
  classDateDp: any;

  buildingsDropdownSettings = {};
  classRoomsDropdownSettings = {};
  classRoomsDisabledDropdownSettings = {};

  editForm = this.fb.group({
    id: [],
    subject: [null, [Validators.required]],
    classDate: [null, [Validators.required]],
    schoolGroup: [null, Validators.required],
    building: [null, Validators.required],
    classRoom: [null, Validators.required],
    startTime: [null, Validators.required],
    classDuration: [null, Validators.required],
    endTime: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected timetableService: TimetableService,
    protected schoolGroupService: SchoolGroupService,
    protected buildingService: BuildingService,
    protected classRoomService: ClassRoomService,
    protected classHoursService: ClassHoursService,
    protected classDurationService: ClassDurationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected languageService: JhiLanguageService
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ timetable }) => {
      this.updateForm(timetable);
    });
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

    if (this.editForm.get(['building']).value !== undefined || null) {
      this.selectedBuilding = [this.editForm.get(['building']).value];
    }

    if (this.editForm.get(['classRoom']).value !== undefined) {
      this.selectedClassRoom = [this.editForm.get(['classRoom']).value];
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

  onSelectBuildingLabel() {
    return this.getLabel('Select building', 'Wybierz budynek');
  }

  onSelectClassRoomLabel() {
    return this.getLabel('Select classroom', 'Wybierz salę');
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

  updateForm(timetable: ITimetable) {
    this.editForm.patchValue({
      id: timetable.id,
      subject: timetable.subject,
      classDate: timetable.classDate,
      schoolGroup: timetable.schoolGroup,
      building: timetable.building,
      classRoom: timetable.classRoom,
      startTime: timetable.startTime,
      classDuration: timetable.classDuration,
      endTime: timetable.endTime
    });

    if (timetable.building) {
      this.getClassRoomByBuildingId(timetable.building);
    }
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const timetable = this.createFromForm();
    if (timetable.id !== undefined) {
      this.subscribeToSaveResponse(this.timetableService.update(timetable));
    } else {
      this.subscribeToSaveResponse(this.timetableService.create(timetable));
    }
  }

  private createFromForm(): ITimetable {
    return {
      ...new Timetable(),
      id: this.editForm.get(['id']).value,
      subject: this.editForm.get(['subject']).value,
      classDate: this.editForm.get(['classDate']).value,
      schoolGroup: this.editForm.get(['schoolGroup']).value,
      building: this.editForm.get(['building']).value[0],
      classRoom: this.editForm.get(['classRoom']).value != null ? this.editForm.get(['classRoom']).value[0] : undefined,
      startTime: this.editForm.get(['startTime']).value,
      classDuration: this.editForm.get(['classDuration']).value,
      endTime: this.editForm.get(['endTime']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITimetable>>) {
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
}
