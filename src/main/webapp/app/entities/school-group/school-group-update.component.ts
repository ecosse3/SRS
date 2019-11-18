import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ISchoolGroup, SchoolGroup } from 'app/shared/model/school-group.model';
import { SchoolGroupService } from './school-group.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IMajor } from 'app/shared/model/major.model';
import { MajorService } from 'app/entities/major/major.service';

@Component({
  selector: 'jhi-school-group-update',
  templateUrl: './school-group-update.component.html'
})
export class SchoolGroupUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  majors: IMajor[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    starost: [],
    major: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected schoolGroupService: SchoolGroupService,
    protected userService: UserService,
    protected majorService: MajorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ schoolGroup }) => {
      this.updateForm(schoolGroup);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.majorService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMajor[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMajor[]>) => response.body)
      )
      .subscribe((res: IMajor[]) => (this.majors = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(schoolGroup: ISchoolGroup) {
    this.editForm.patchValue({
      id: schoolGroup.id,
      name: schoolGroup.name,
      starost: schoolGroup.starost,
      major: schoolGroup.major
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const schoolGroup = this.createFromForm();
    if (schoolGroup.id !== undefined) {
      this.subscribeToSaveResponse(this.schoolGroupService.update(schoolGroup));
    } else {
      this.subscribeToSaveResponse(this.schoolGroupService.create(schoolGroup));
    }
  }

  private createFromForm(): ISchoolGroup {
    return {
      ...new SchoolGroup(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      starost: this.editForm.get(['starost']).value,
      major: this.editForm.get(['major']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISchoolGroup>>) {
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

  trackMajorById(index: number, item: IMajor) {
    return item.id;
  }
}
