import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IClassModel, ClassModel } from 'app/shared/model/class-model.model';
import { ClassModelService } from './class-model.service';
import { IClassRoom } from 'app/shared/model/class-room.model';
import { ClassRoomService } from 'app/entities/class-room/class-room.service';

@Component({
  selector: 'jhi-class-model-update',
  templateUrl: './class-model-update.component.html'
})
export class ClassModelUpdateComponent implements OnInit {
  isSaving: boolean;

  classrooms: IClassRoom[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected classModelService: ClassModelService,
    protected classRoomService: ClassRoomService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ classModel }) => {
      this.updateForm(classModel);
    });
    this.classRoomService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClassRoom[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClassRoom[]>) => response.body)
      )
      .subscribe((res: IClassRoom[]) => (this.classrooms = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(classModel: IClassModel) {
    this.editForm.patchValue({
      id: classModel.id,
      name: classModel.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const classModel = this.createFromForm();
    if (classModel.id !== undefined) {
      this.subscribeToSaveResponse(this.classModelService.update(classModel));
    } else {
      this.subscribeToSaveResponse(this.classModelService.create(classModel));
    }
  }

  private createFromForm(): IClassModel {
    return {
      ...new ClassModel(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassModel>>) {
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

  trackClassRoomById(index: number, item: IClassRoom) {
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
