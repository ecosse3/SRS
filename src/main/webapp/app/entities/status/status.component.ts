import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IStatus } from 'app/shared/model/status.model';
import { AccountService } from 'app/core/auth/account.service';
import { StatusService } from './status.service';

@Component({
  selector: 'jhi-status',
  templateUrl: './status.component.html'
})
export class StatusComponent implements OnInit, OnDestroy {
  statuses: IStatus[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected statusService: StatusService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.statusService
      .query()
      .pipe(
        filter((res: HttpResponse<IStatus[]>) => res.ok),
        map((res: HttpResponse<IStatus[]>) => res.body)
      )
      .subscribe((res: IStatus[]) => {
        this.statuses = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInStatuses();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IStatus) {
    return item.id;
  }

  registerChangeInStatuses() {
    this.eventSubscriber = this.eventManager.subscribe('statusListModification', response => this.loadAll());
  }
}
