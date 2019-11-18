import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IClassRoom, ClassRoom } from 'app/shared/model/class-room.model';
import { ClassRoomService } from './class-room.service';
import { IClassModel } from 'app/shared/model/class-model.model';
import { ClassModelService } from 'app/entities/class-model/class-model.service';
import { IBuilding } from 'app/shared/model/building.model';
import { BuildingService } from 'app/entities/building/building.service';

@Component({
  selector: 'jhi-class-room-update',
  templateUrl: './class-room-update.component.html'
})
export class ClassRoomUpdateComponent implements OnInit {
  isSaving: boolean;

  classmodels: IClassModel[];

  buildings: IBuilding[];

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]],
    maximumSize: [null, [Validators.min(2), Validators.max(150)]],
    classModels: [],
    building: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected classRoomService: ClassRoomService,
    protected classModelService: ClassModelService,
    protected buildingService: BuildingService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ classRoom }) => {
      this.updateForm(classRoom);
    });
    this.classModelService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClassModel[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClassModel[]>) => response.body)
      )
      .subscribe((res: IClassModel[]) => (this.classmodels = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.buildingService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBuilding[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBuilding[]>) => response.body)
      )
      .subscribe((res: IBuilding[]) => (this.buildings = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(classRoom: IClassRoom) {
    this.editForm.patchValue({
      id: classRoom.id,
      number: classRoom.number,
      maximumSize: classRoom.maximumSize,
      classModels: classRoom.classModels,
      building: classRoom.building
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const classRoom = this.createFromForm();
    if (classRoom.id !== undefined) {
      this.subscribeToSaveResponse(this.classRoomService.update(classRoom));
    } else {
      this.subscribeToSaveResponse(this.classRoomService.create(classRoom));
    }
  }

  private createFromForm(): IClassRoom {
    return {
      ...new ClassRoom(),
      id: this.editForm.get(['id']).value,
      number: this.editForm.get(['number']).value,
      maximumSize: this.editForm.get(['maximumSize']).value,
      classModels: this.editForm.get(['classModels']).value,
      building: this.editForm.get(['building']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassRoom>>) {
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

  trackClassModelById(index: number, item: IClassModel) {
    return item.id;
  }

  trackBuildingById(index: number, item: IBuilding) {
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
