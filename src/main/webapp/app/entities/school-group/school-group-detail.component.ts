import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISchoolGroup } from 'app/shared/model/school-group.model';

@Component({
  selector: 'jhi-school-group-detail',
  templateUrl: './school-group-detail.component.html'
})
export class SchoolGroupDetailComponent implements OnInit {
  schoolGroup: ISchoolGroup;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ schoolGroup }) => {
      this.schoolGroup = schoolGroup;
    });
  }

  previousState() {
    window.history.back();
  }
}
