import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IMyReservations } from 'app/shared/model/my-reservations.model';
import { IReservation } from 'app/shared/model/reservation.model';
import { AccountService } from 'app/core/auth/account.service';
import { MyReservationsService } from './my-reservations.service';

@Component({
  selector: 'jhi-my-reservations',
  templateUrl: './my-reservations.component.html'
})
export class MyReservationsComponent implements OnInit, OnDestroy {
  myReservations: IMyReservations[];
  currentAccount: any;
  eventSubscriber: Subscription;
  userReservations: any;
  userReservationsCount: any;

  constructor(
    protected myReservationsService: MyReservationsService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.myReservationsService
      .query()
      .pipe(
        filter((res: HttpResponse<IMyReservations[]>) => res.ok),
        map((res: HttpResponse<IMyReservations[]>) => res.body)
      )
      .subscribe((res: IMyReservations[]) => {
        this.myReservations = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInMyReservations();
    this.getCountUserReservations(this.currentAccount.email);
    this.getUserReservations(this.currentAccount.email);
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IMyReservations) {
    return item.id;
  }

  registerChangeInMyReservations() {
    this.eventSubscriber = this.eventManager.subscribe('myReservationsListModification', response => this.loadAll());
  }

  getUserReservations(currentAccount: any) {
    this.myReservationsService.getUserReservationsByAccountName(currentAccount).subscribe(res => {
      this.userReservations = res.body;
    });
  }

  getCountUserReservations(currentAccount: any) {
    this.myReservationsService.getCountUserReservationsByAccountName(currentAccount).subscribe(res => {
      this.userReservationsCount = res.body;
    });
  }
}
