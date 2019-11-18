import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMajor, Major } from 'app/shared/model/major.model';
import { MajorService } from './major.service';
import { IDepartment } from 'app/shared/model/department.model';
import { DepartmentService } from 'app/entities/department/department.service';

@Component({
  selector: 'jhi-major-update',
  templateUrl: './major-update.component.html'
})
export class MajorUpdateComponent implements OnInit {
  isSaving: boolean;

  departments: IDepartment[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    department: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected majorService: MajorService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ major }) => {
      this.updateForm(major);
    });
    this.departmentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IDepartment[]>) => mayBeOk.ok),
        map((response: HttpResponse<IDepartment[]>) => response.body)
      )
      .subscribe((res: IDepartment[]) => (this.departments = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(major: IMajor) {
    this.editForm.patchValue({
      id: major.id,
      name: major.name,
      department: major.department
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const major = this.createFromForm();
    if (major.id !== undefined) {
      this.subscribeToSaveResponse(this.majorService.update(major));
    } else {
      this.subscribeToSaveResponse(this.majorService.create(major));
    }
  }

  private createFromForm(): IMajor {
    return {
      ...new Major(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      department: this.editForm.get(['department']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMajor>>) {
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

  trackDepartmentById(index: number, item: IDepartment) {
    return item.id;
  }
}
