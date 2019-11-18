import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMajor } from 'app/shared/model/major.model';

@Component({
  selector: 'jhi-major-detail',
  templateUrl: './major-detail.component.html'
})
export class MajorDetailComponent implements OnInit {
  major: IMajor;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ major }) => {
      this.major = major;
    });
  }

  previousState() {
    window.history.back();
  }
}
