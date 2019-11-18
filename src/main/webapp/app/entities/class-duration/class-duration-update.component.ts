import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IClassDuration, ClassDuration } from 'app/shared/model/class-duration.model';
import { ClassDurationService } from './class-duration.service';

@Component({
  selector: 'jhi-class-duration-update',
  templateUrl: './class-duration-update.component.html'
})
export class ClassDurationUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]]
  });

  constructor(protected classDurationService: ClassDurationService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ classDuration }) => {
      this.updateForm(classDuration);
    });
  }

  updateForm(classDuration: IClassDuration) {
    this.editForm.patchValue({
      id: classDuration.id,
      name: classDuration.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const classDuration = this.createFromForm();
    if (classDuration.id !== undefined) {
      this.subscribeToSaveResponse(this.classDurationService.update(classDuration));
    } else {
      this.subscribeToSaveResponse(this.classDurationService.create(classDuration));
    }
  }

  private createFromForm(): IClassDuration {
    return {
      ...new ClassDuration(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassDuration>>) {
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
