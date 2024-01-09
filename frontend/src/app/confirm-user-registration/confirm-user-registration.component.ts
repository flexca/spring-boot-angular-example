import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CompleteUserRegistrationRequest } from './complete-user-registration.request';
import { ConfirmUserRegistrationService } from './confirm-user-registration.service';
import { ValidateTokenRequest } from '../confirm-registration/validate-token.request';
import { InfoMessageType } from '../info-message/info-message.component';

@Component({
  selector: 'app-confirm-user-registration',
  templateUrl: './confirm-user-registration.component.html',
  styleUrls: ['./confirm-user-registration.component.scss']
})
export class ConfirmUserRegistrationComponent implements OnInit {

  token = this.route.snapshot.params['token'];
  userId = this.route.snapshot.params['userId'];
  tokenValid = false;

  confirmationError = false;
  confirmationErrorMessage = "";

  hidePassword = true;
  hideReenterPassword = true;

  errorMessageType = InfoMessageType.info;
  
  constructor(private route: ActivatedRoute, private router: Router, private confirmUserRegistrationService: ConfirmUserRegistrationService) {
  }

  model = new CompleteUserRegistrationRequest(this.token);

  ngOnInit() {
    this.confirmUserRegistrationService.validateToken(this.userId, new ValidateTokenRequest(this.token)).subscribe({
      next: (v) => {
        this.tokenValid = true;
      },
      error: (e) => {
        this.tokenValid = false;
      },
      complete: () => {} 
    });
  }

  onSubmit() {
    this.confirmUserRegistrationService.completeRegistration(this.userId, this.model).subscribe({
      next: (v) => {
        this.router.navigate(['/login']);
      },
      error: (e) => {
        this.confirmationError = true;
        if (e.error) {
          this.confirmationErrorMessage = e.error['message'];
        } else {
          this.confirmationErrorMessage = "Unexpected error. Please try again later.";
        }
      },
      complete: () => {} 
    });
  }

}
