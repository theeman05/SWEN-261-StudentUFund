import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FundingLeaderboardComponent } from './funding-leaderboard.component';

describe('FundingLeaderboardComponent', () => {
  let component: FundingLeaderboardComponent;
  let fixture: ComponentFixture<FundingLeaderboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FundingLeaderboardComponent]
    });
    fixture = TestBed.createComponent(FundingLeaderboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
