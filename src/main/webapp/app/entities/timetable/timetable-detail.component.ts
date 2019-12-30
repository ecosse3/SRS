import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITimetable } from 'app/shared/model/timetable.model';

@Component({
  selector: 'jhi-timetable-detail',
  templateUrl: './timetable-detail.component.html'
})
export class TimetableDetailComponent implements OnInit {
  timetable: ITimetable;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ timetable }) => {
      this.timetable = timetable;
    });
  }

  previousState() {
    window.history.back();
  }
}
