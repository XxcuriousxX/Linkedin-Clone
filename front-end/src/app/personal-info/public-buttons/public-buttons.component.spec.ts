import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PublicButtonsComponent } from './public-buttons.component';

describe('PublicButtonsComponent', () => {
  let component: PublicButtonsComponent;
  let fixture: ComponentFixture<PublicButtonsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PublicButtonsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PublicButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
