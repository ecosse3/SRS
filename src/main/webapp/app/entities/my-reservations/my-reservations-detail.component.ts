import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMyReservations } from 'app/shared/model/my-reservations.model';

@Component({
  selector: 'jhi-my-reservations-detail',
  templateUrl: './my-reservations-detail.component.html'
})
export class MyReservationsDetailComponent implements OnInit {
  myReservations: IMyReservations;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ myReservations }) => {
      this.myReservations = myReservations;
    });
  }

  previousState() {
    window.history.back();
  }
}
