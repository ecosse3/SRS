import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IClassModel } from 'app/shared/model/class-model.model';
import { AccountService } from 'app/core/auth/account.service';
import { ClassModelService } from './class-model.service';

@Component({
  selector: 'jhi-class-model',
  templateUrl: './class-model.component.html'
})
export class ClassModelComponent implements OnInit, OnDestroy {
  classModels: IClassModel[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected classModelService: ClassModelService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.classModelService
      .query()
      .pipe(
        filter((res: HttpResponse<IClassModel[]>) => res.ok),
        map((res: HttpResponse<IClassModel[]>) => res.body)
      )
      .subscribe((res: IClassModel[]) => {
        this.classModels = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInClassModels();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IClassModel) {
    return item.id;
  }

  registerChangeInClassModels() {
    this.eventSubscriber = this.eventManager.subscribe('classModelListModification', response => this.loadAll());
  }
}
