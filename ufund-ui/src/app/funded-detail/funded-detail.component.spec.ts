import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FundedDetailComponent } from './funded-detail.component';

describe('FundedDetailComponent', () => {
  let component: FundedDetailComponent;
  let fixture: ComponentFixture<FundedDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FundedDetailComponent]
    });
    fixture = TestBed.createComponent(FundedDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
