import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IClassHours, ClassHours } from 'app/shared/model/class-hours.model';
import { ClassHoursService } from './class-hours.service';

@Component({
  selector: 'jhi-class-hours-update',
  templateUrl: './class-hours-update.component.html'
})
export class ClassHoursUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    startTime: [null, [Validators.required]],
    endTime: [null, [Validators.required]]
  });

  constructor(protected classHoursService: ClassHoursService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ classHours }) => {
      this.updateForm(classHours);
    });
  }

  updateForm(classHours: IClassHours) {
    this.editForm.patchValue({
      id: classHours.id,
      startTime: classHours.startTime,
      endTime: classHours.endTime
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const classHours = this.createFromForm();
    if (classHours.id !== undefined) {
      this.subscribeToSaveResponse(this.classHoursService.update(classHours));
    } else {
      this.subscribeToSaveResponse(this.classHoursService.create(classHours));
    }
  }

  private createFromForm(): IClassHours {
    return {
      ...new ClassHours(),
      id: this.editForm.get(['id']).value,
      startTime: this.editForm.get(['startTime']).value,
      endTime: this.editForm.get(['endTime']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassHours>>) {
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
