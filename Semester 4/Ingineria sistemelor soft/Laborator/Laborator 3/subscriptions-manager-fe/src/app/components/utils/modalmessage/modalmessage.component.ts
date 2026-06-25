import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-modalmessage',
  templateUrl: './modalmessage.component.html',
  styleUrl: './modalmessage.component.css',
  standalone: true
})
export class ModalmessageComponent {
  @Input() message: string = '';
  @Output() close = new EventEmitter<void>();

  onOkClick(): void {
    this.close.emit();
  }
}
