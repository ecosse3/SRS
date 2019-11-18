import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IClassHours } from 'app/shared/model/class-hours.model';
import { AccountService } from 'app/core/auth/account.service';
import { ClassHoursService } from './class-hours.service';

@Component({
  selector: 'jhi-class-hours',
  templateUrl: './class-hours.component.html'
})
export class ClassHoursComponent implements OnInit, OnDestroy {
  classHours: IClassHours[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected classHoursService: ClassHoursService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.classHoursService
      .query()
      .pipe(
        filter((res: HttpResponse<IClassHours[]>) => res.ok),
        map((res: HttpResponse<IClassHours[]>) => res.body)
      )
      .subscribe((res: IClassHours[]) => {
        this.classHours = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInClassHours();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IClassHours) {
    return item.id;
  }

  registerChangeInClassHours() {
    this.eventSubscriber = this.eventManager.subscribe('classHoursListModification', response => this.loadAll());
  }
}
