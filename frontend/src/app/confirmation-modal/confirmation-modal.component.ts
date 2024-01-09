import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.scss']
})
export class ConfirmationModalComponent {

  @Input() title = "";
  @Input() content = "";
  @Input() okButtonCaption = "";
  @Input() cancelButtonCaption = "";

  @Output() okClicked = new EventEmitter<void>();
  @Output() cancelClicked = new EventEmitter<void>();

  okAction() {
    this.okClicked.emit();
  }

  cancelAction() {
    this.cancelClicked.emit();
  }
}
