import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedDetailSupporterComponent } from './need-detail-supporter.component';

describe('NeedDetailSupporterComponent', () => {
  let component: NeedDetailSupporterComponent;
  let fixture: ComponentFixture<NeedDetailSupporterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NeedDetailSupporterComponent]
    });
    fixture = TestBed.createComponent(NeedDetailSupporterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
