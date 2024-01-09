import { Component, Input } from '@angular/core';

export enum InfoMessageType {
  info,
  error,
}

@Component({
  selector: 'app-info-message',
  templateUrl: './info-message.component.html',
  styleUrls: ['./info-message.component.scss']
})
export class InfoMessageComponent {

  @Input() visible = false;
  @Input() type = InfoMessageType.info;
  @Input() message = "";
}
