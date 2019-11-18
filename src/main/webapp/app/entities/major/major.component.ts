import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IMajor } from 'app/shared/model/major.model';
import { AccountService } from 'app/core/auth/account.service';
import { MajorService } from './major.service';

@Component({
  selector: 'jhi-major',
  templateUrl: './major.component.html'
})
export class MajorComponent implements OnInit, OnDestroy {
  majors: IMajor[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected majorService: MajorService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.majorService
      .query()
      .pipe(
        filter((res: HttpResponse<IMajor[]>) => res.ok),
        map((res: HttpResponse<IMajor[]>) => res.body)
      )
      .subscribe((res: IMajor[]) => {
        this.majors = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInMajors();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMajor) {
    return item.id;
  }

  registerChangeInMajors() {
    this.eventSubscriber = this.eventManager.subscribe('majorListModification', response => this.loadAll());
  }
}
