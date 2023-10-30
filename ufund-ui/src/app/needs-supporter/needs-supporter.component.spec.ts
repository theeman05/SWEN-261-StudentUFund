import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedsSupporterComponent } from './needs-supporter.component';

describe('NeedsSupporterComponent', () => {
  let component: NeedsSupporterComponent;
  let fixture: ComponentFixture<NeedsSupporterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NeedsSupporterComponent]
    });
    fixture = TestBed.createComponent(NeedsSupporterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
