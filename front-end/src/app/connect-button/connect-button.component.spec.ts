import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConnectButtonComponent } from './connect-button.component';

describe('ConnectButtonComponent', () => {
  let component: ConnectButtonComponent;
  let fixture: ComponentFixture<ConnectButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConnectButtonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConnectButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
