import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassModel } from 'app/shared/model/class-model.model';

@Component({
  selector: 'jhi-class-model-detail',
  templateUrl: './class-model-detail.component.html'
})
export class ClassModelDetailComponent implements OnInit {
  classModel: IClassModel;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ classModel }) => {
      this.classModel = classModel;
    });
  }

  previousState() {
    window.history.back();
  }
}
