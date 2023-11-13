import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { NeedsComponent } from './needs/needs.component';
import { AppRoutingModule } from './app-routing.module';
import { NeedDetailComponent } from './need-detail/need-detail.component';
import { BasketComponent } from './basket/basket.component';
import { NeedCreateComponent } from './need-create/need-create.component';
import { NeedDetailSupporterComponent } from './need-detail-supporter/need-detail-supporter.component';
import { LogoutComponent } from './logout/logout.component';
import { NeedsSupporterComponent } from './needs-supporter/needs-supporter.component';
import { AdminComponent } from './admin/admin.component';
import { SupporterComponent } from './supporter/supporter.component';
import { NeedSearchComponent } from './need-search/need-search.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { ErrorComponent } from './error/error.component';
import { FundingLeaderboardComponent } from './funding-leaderboard/funding-leaderboard.component';
import { AllFundedComponent } from './all-funded/all-funded.component';
import { NeedMessagesComponent } from './need-messages/need-messages.component';
import { FundedDetailComponent } from './funded-detail/funded-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    NeedsComponent,
    NeedDetailComponent,
    BasketComponent,
    NeedCreateComponent,
    NeedDetailSupporterComponent,
    LogoutComponent,
    NeedsSupporterComponent,
    AdminComponent,
    SupporterComponent,
    NeedSearchComponent,
    CheckoutComponent,
    ErrorComponent,
    FundingLeaderboardComponent,
    AllFundedComponent,
    NeedMessagesComponent,
    FundedDetailComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
