import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { NeedDetailComponent } from './need-detail/need-detail.component';
import { NeedDetailSupporterComponent } from './need-detail-supporter/need-detail-supporter.component';
import { NeedsComponent } from './needs/needs.component';
import { BasketComponent } from './basket/basket.component';
import { NeedCreateComponent } from './need-create/need-create.component';
import { LoginComponent } from './login/login.component';
import { NeedsSupporterComponent } from './needs-supporter/needs-supporter.component';
import { AdminComponent } from './admin/admin.component';
import { LogoutComponent } from './logout/logout.component';
import { SupporterComponent } from './supporter/supporter.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { FundingLeaderboardComponent } from './funding-leaderboard/funding-leaderboard.component';
import { AllFundedComponent } from './all-funded/all-funded.component';
import { NeedMessagesComponent } from './need-messages/need-messages.component';
import { FundedDetailComponent } from './funded-detail/funded-detail.component';
import { SupporterDetailComponent } from './supporter-detail/supporter-detail.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent },
  { path: 'admin',
    component: AdminComponent,
    children: [
      { path: 'needs', component: NeedsComponent },
      { path: 'create', component: NeedCreateComponent },
      { path: 'detail/:name', component: NeedDetailComponent },
      { path: 'funded-needs', component: AllFundedComponent },
      { path: 'funded/:to_username/:need_name', component: FundedDetailComponent },
      { path: 'leaderboard', component: FundingLeaderboardComponent },
      { path: 'funded/:username', component: SupporterDetailComponent }
    ]
  },
  { path: 'supporter',
    component: SupporterComponent,
    children: [
      { path: 'needs', component: NeedsSupporterComponent },
      { path: 'basket', component: BasketComponent },
      { path: 'detail/:name', component: NeedDetailSupporterComponent} ,
      { path: 'basket/checkout', component: CheckoutComponent },
      { path: 'inbox', component: NeedMessagesComponent },
      { path: 'leaderboard', component: FundingLeaderboardComponent },
      { path: 'funded/:username', component: SupporterDetailComponent }
    ]
  },
];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
