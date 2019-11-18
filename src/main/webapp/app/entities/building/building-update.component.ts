import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IBuilding, Building } from 'app/shared/model/building.model';
import { BuildingService } from './building.service';

@Component({
  selector: 'jhi-building-update',
  templateUrl: './building-update.component.html'
})
export class BuildingUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    number: [null, [Validators.required]]
  });

  constructor(protected buildingService: BuildingService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ building }) => {
      this.updateForm(building);
    });
  }

  updateForm(building: IBuilding) {
    this.editForm.patchValue({
      id: building.id,
      number: building.number
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const building = this.createFromForm();
    if (building.id !== undefined) {
      this.subscribeToSaveResponse(this.buildingService.update(building));
    } else {
      this.subscribeToSaveResponse(this.buildingService.create(building));
    }
  }

  private createFromForm(): IBuilding {
    return {
      ...new Building(),
      id: this.editForm.get(['id']).value,
      number: this.editForm.get(['number']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBuilding>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
