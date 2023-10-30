import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedSearchComponent } from './need-search.component';

describe('NeedSearchComponent', () => {
  let component: NeedSearchComponent;
  let fixture: ComponentFixture<NeedSearchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NeedSearchComponent]
    });
    fixture = TestBed.createComponent(NeedSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
