import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalmessageComponent } from './modalmessage.component';

describe('ModalmessageComponent', () => {
  let component: ModalmessageComponent;
  let fixture: ComponentFixture<ModalmessageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalmessageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalmessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
