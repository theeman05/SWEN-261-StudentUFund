import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardComponent } from './dashboard/dashboard.component';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedsComponent } from './needs/needs.component';
import { BasketComponent } from './basket/basket.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'detail/:name', component: NeedDetailComponent },
  { path: 'needs', component: NeedsComponent },
  {path: 'basket', component: BasketComponent}
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
