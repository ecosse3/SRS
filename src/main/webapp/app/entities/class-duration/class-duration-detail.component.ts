import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassDuration } from 'app/shared/model/class-duration.model';

@Component({
  selector: 'jhi-class-duration-detail',
  templateUrl: './class-duration-detail.component.html'
})
export class ClassDurationDetailComponent implements OnInit {
  classDuration: IClassDuration;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ classDuration }) => {
      this.classDuration = classDuration;
    });
  }

  previousState() {
    window.history.back();
  }
}
