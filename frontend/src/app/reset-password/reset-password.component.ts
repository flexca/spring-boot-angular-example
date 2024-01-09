import { Component } from '@angular/core';
import { ResetPasswordRequest } from './reset-password.request';
import { ResetPasswordService } from './reset-password.service';
import { Router } from '@angular/router';
import { InfoMessageType } from '../info-message/info-message.component';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {

  constructor(private resetPasswordService: ResetPasswordService, private router: Router) {
  }

  resetPasswordError = false;
  resetPasswordErrorMessage = "";
  errorMessageType = InfoMessageType.info;
  resetPasswordDone = false;

  model = new ResetPasswordRequest();

  onSubmit() {
    this.resetPasswordService.resetPassword(this.model).subscribe({
      next: (response) => {
        this.resetPasswordDone = true;
        this.resetPasswordError = false;
        this.resetPasswordErrorMessage = "";
      },
      error: (e) => {
        this.resetPasswordError = true;
        if (e.error && e.error['message']) {
          this.resetPasswordErrorMessage = e.error['message'];
        } else {
          this.resetPasswordErrorMessage = "Unexpected error. Please try again later.";
        }
      }
    });
  }

  cancel() {
    this.router.navigate(['/login'])
  }
}
