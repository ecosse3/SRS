import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SrsSharedModule } from 'app/shared/shared.module';
import { ClassModelComponent } from './class-model.component';
import { ClassModelDetailComponent } from './class-model-detail.component';
import { ClassModelUpdateComponent } from './class-model-update.component';
import { ClassModelDeletePopupComponent, ClassModelDeleteDialogComponent } from './class-model-delete-dialog.component';
import { classModelRoute, classModelPopupRoute } from './class-model.route';

const ENTITY_STATES = [...classModelRoute, ...classModelPopupRoute];

@NgModule({
  imports: [SrsSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ClassModelComponent,
    ClassModelDetailComponent,
    ClassModelUpdateComponent,
    ClassModelDeleteDialogComponent,
    ClassModelDeletePopupComponent
  ],
  entryComponents: [ClassModelDeleteDialogComponent]
})
export class SrsClassModelModule {}
