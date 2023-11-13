import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedMessagesComponent } from './need-messages.component';

describe('NeedMessagesComponent', () => {
  let component: NeedMessagesComponent;
  let fixture: ComponentFixture<NeedMessagesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NeedMessagesComponent]
    });
    fixture = TestBed.createComponent(NeedMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
