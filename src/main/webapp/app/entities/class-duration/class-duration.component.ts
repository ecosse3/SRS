import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IClassDuration } from 'app/shared/model/class-duration.model';
import { AccountService } from 'app/core/auth/account.service';
import { ClassDurationService } from './class-duration.service';

@Component({
  selector: 'jhi-class-duration',
  templateUrl: './class-duration.component.html'
})
export class ClassDurationComponent implements OnInit, OnDestroy {
  classDurations: IClassDuration[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected classDurationService: ClassDurationService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.classDurationService
      .query()
      .pipe(
        filter((res: HttpResponse<IClassDuration[]>) => res.ok),
        map((res: HttpResponse<IClassDuration[]>) => res.body)
      )
      .subscribe((res: IClassDuration[]) => {
        this.classDurations = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInClassDurations();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IClassDuration) {
    return item.id;
  }

  registerChangeInClassDurations() {
    this.eventSubscriber = this.eventManager.subscribe('classDurationListModification', response => this.loadAll());
  }
}
