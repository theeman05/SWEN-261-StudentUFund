import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupporterDetailComponent } from './supporter-detail.component';

describe('SupporterDetailComponent', () => {
  let component: SupporterDetailComponent;
  let fixture: ComponentFixture<SupporterDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SupporterDetailComponent]
    });
    fixture = TestBed.createComponent(SupporterDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
