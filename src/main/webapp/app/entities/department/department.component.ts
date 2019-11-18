import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IDepartment } from 'app/shared/model/department.model';
import { AccountService } from 'app/core/auth/account.service';
import { DepartmentService } from './department.service';

@Component({
  selector: 'jhi-department',
  templateUrl: './department.component.html'
})
export class DepartmentComponent implements OnInit, OnDestroy {
  departments: IDepartment[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected departmentService: DepartmentService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.departmentService
      .query()
      .pipe(
        filter((res: HttpResponse<IDepartment[]>) => res.ok),
        map((res: HttpResponse<IDepartment[]>) => res.body)
      )
      .subscribe((res: IDepartment[]) => {
        this.departments = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInDepartments();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IDepartment) {
    return item.id;
  }

  registerChangeInDepartments() {
    this.eventSubscriber = this.eventManager.subscribe('departmentListModification', response => this.loadAll());
  }
}
