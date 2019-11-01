import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { SrsSharedModule } from 'app/shared/shared.module';
import { SrsCoreModule } from 'app/core/core.module';
import { SrsAppRoutingModule } from './app-routing.module';
import { SrsHomeModule } from './home/home.module';
import { SrsEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    SrsSharedModule,
    SrsCoreModule,
    SrsHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    SrsEntityModule,
    SrsAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class SrsAppModule {}
