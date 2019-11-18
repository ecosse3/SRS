import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SrsTestModule } from '../../../test.module';
import { ClassRoomDetailComponent } from 'app/entities/class-room/class-room-detail.component';
import { ClassRoom } from 'app/shared/model/class-room.model';

describe('Component Tests', () => {
  describe('ClassRoom Management Detail Component', () => {
    let comp: ClassRoomDetailComponent;
    let fixture: ComponentFixture<ClassRoomDetailComponent>;
    const route = ({ data: of({ classRoom: new ClassRoom(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [SrsTestModule],
        declarations: [ClassRoomDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ClassRoomDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassRoomDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classRoom).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
