import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBuilding } from 'app/shared/model/building.model';

@Component({
  selector: 'jhi-building-detail',
  templateUrl: './building-detail.component.html'
})
export class BuildingDetailComponent implements OnInit {
  building: IBuilding;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ building }) => {
      this.building = building;
    });
  }

  previousState() {
    window.history.back();
  }
}
