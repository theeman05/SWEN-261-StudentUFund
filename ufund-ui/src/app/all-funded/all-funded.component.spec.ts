import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AllFundedComponent } from './all-funded.component';

describe('AllFundedComponent', () => {
  let component: AllFundedComponent;
  let fixture: ComponentFixture<AllFundedComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AllFundedComponent]
    });
    fixture = TestBed.createComponent(AllFundedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
