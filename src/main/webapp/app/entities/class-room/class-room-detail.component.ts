import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassRoom } from 'app/shared/model/class-room.model';

@Component({
  selector: 'jhi-class-room-detail',
  templateUrl: './class-room-detail.component.html'
})
export class ClassRoomDetailComponent implements OnInit {
  classRoom: IClassRoom;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ classRoom }) => {
      this.classRoom = classRoom;
    });
  }

  previousState() {
    window.history.back();
  }
}
