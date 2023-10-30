import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedCreateComponent } from './need-create.component';

describe('NeedCreateComponent', () => {
  let component: NeedCreateComponent;
  let fixture: ComponentFixture<NeedCreateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NeedCreateComponent]
    });
    fixture = TestBed.createComponent(NeedCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
