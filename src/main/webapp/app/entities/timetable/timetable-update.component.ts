import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { ITimetable, Timetable } from 'app/shared/model/timetable.model';
import { TimetableService } from './timetable.service';

@Component({
  selector: 'jhi-timetable-update',
  templateUrl: './timetable-update.component.html'
})
export class TimetableUpdateComponent implements OnInit {
  isSaving: boolean;
  classDateDp: any;

  editForm = this.fb.group({
    id: [],
    subject: [null, [Validators.required]],
    classDate: [null, [Validators.required]]
  });

  constructor(protected timetableService: TimetableService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ timetable }) => {
      this.updateForm(timetable);
    });
  }

  updateForm(timetable: ITimetable) {
    this.editForm.patchValue({
      id: timetable.id,
      subject: timetable.subject,
      classDate: timetable.classDate
    });
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
      classDate: this.editForm.get(['classDate']).value
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
}
