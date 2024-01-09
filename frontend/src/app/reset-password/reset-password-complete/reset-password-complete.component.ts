import { Component } from '@angular/core';
import { ResetPasswordService } from '../reset-password.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ValidateTokenRequest } from 'src/app/confirm-registration/validate-token.request';
import { CompleteUserRegistrationRequest } from 'src/app/confirm-user-registration/complete-user-registration.request';
import { InfoMessageType } from 'src/app/info-message/info-message.component';

@Component({
  selector: 'app-reset-password-complete',
  templateUrl: './reset-password-complete.component.html',
  styleUrls: ['./reset-password-complete.component.scss']
})
export class ResetPasswordCompleteComponent {

  constructor(private route: ActivatedRoute, private router: Router, private resetPasswordService: ResetPasswordService) {
  }

  token = this.route.snapshot.params['token'];
  userId = this.route.snapshot.params['userId'];
  tokenValid = false;
  hidePassword = true;
  hideReenterPassword = true;

  completeResetError = false;
  completeResetErrorMessage = "";
  
  errorMessageType = InfoMessageType.info;

  model = new CompleteUserRegistrationRequest(this.token);

  ngOnInit() {
    this.resetPasswordService.validateToken(this.userId, new ValidateTokenRequest(this.token)).subscribe({
      next: (v) => {
        this.tokenValid = true;
      },
      error: (e) => {
        this.tokenValid = false;
      }
    });
  }

  onSubmit() {
    this.resetPasswordService.completeResetPassword(this.userId, this.model).subscribe({
      next: (v) => {
        this.router.navigate(["/login"]);
      },
      error: (e) => {
        this.completeResetError = true;
        if (e.error && e.error['message']) {
          this.completeResetErrorMessage = e.error['message'];
        } else {
          this.completeResetErrorMessage = "Unexpected error. Please try again later.";
        }
      }
    });
  }
}
