import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'department',
        loadChildren: () => import('./department/department.module').then(m => m.SrsDepartmentModule)
      },
      {
        path: 'major',
        loadChildren: () => import('./major/major.module').then(m => m.SrsMajorModule)
      },
      {
        path: 'school-group',
        loadChildren: () => import('./school-group/school-group.module').then(m => m.SrsSchoolGroupModule)
      },
      {
        path: 'building',
        loadChildren: () => import('./building/building.module').then(m => m.SrsBuildingModule)
      },
      {
        path: 'class-room',
        loadChildren: () => import('./class-room/class-room.module').then(m => m.SrsClassRoomModule)
      },
      {
        path: 'reservation',
        loadChildren: () => import('./reservation/reservation.module').then(m => m.SrsReservationModule)
      },
      {
        path: 'class-hours',
        loadChildren: () => import('./class-hours/class-hours.module').then(m => m.SrsClassHoursModule)
      },
      {
        path: 'class-duration',
        loadChildren: () => import('./class-duration/class-duration.module').then(m => m.SrsClassDurationModule)
      },
      {
        path: 'class-model',
        loadChildren: () => import('./class-model/class-model.module').then(m => m.SrsClassModelModule)
      },
      {
        path: 'my-reservations',
        loadChildren: () => import('./my-reservations/my-reservations.module').then(m => m.SrsMyReservationsModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class SrsEntityModule {}
