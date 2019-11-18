import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassHours } from 'app/shared/model/class-hours.model';

@Component({
  selector: 'jhi-class-hours-detail',
  templateUrl: './class-hours-detail.component.html'
})
export class ClassHoursDetailComponent implements OnInit {
  classHours: IClassHours;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ classHours }) => {
      this.classHours = classHours;
    });
  }

  previousState() {
    window.history.back();
  }
}
