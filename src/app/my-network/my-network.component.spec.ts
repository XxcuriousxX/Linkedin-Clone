import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyNetworkComponent } from './my-network.component';

describe('MyNetworkComponent', () => {
  let component: MyNetworkComponent;
  let fixture: ComponentFixture<MyNetworkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MyNetworkComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MyNetworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
