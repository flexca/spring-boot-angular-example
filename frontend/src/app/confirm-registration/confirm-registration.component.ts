import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfirmRegistrationService } from './confirm-registration.service';
import { ValidateTokenRequest } from './validate-token.request';
import { CompleteRegistrationRequest } from './complete-registration.request';
import { InfoMessageType } from '../info-message/info-message.component';

@Component({
  selector: 'app-confirm-registration',
  templateUrl: './confirm-registration.component.html',
  styleUrls: ['./confirm-registration.component.scss']
})
export class ConfirmRegistrationComponent implements OnInit {

    constructor(private route: ActivatedRoute, private router: Router, private confirmRegistrationService: ConfirmRegistrationService) {
    }

    tokenValid = false;
    token = this.route.snapshot.params['token'];
    organizationId = this.route.snapshot.params['organizationId'];

    confirmError = false;
    confirmErrorMessage = "";

    validateTokenError = false;
    validateTokenErrorMessage = "";
    
    hidePassword = true;
    hideReenterPassword = true;

    errorMessageType = InfoMessageType.info;
    
    ngOnInit() {
      
      this.confirmRegistrationService.validateToken(this.organizationId, new ValidateTokenRequest(this.token)).subscribe({
        next: (v) => {
          this.tokenValid = true;
          this.confirmError = false;
          this.confirmErrorMessage = "";
          this.validateTokenError = false;
          this.validateTokenErrorMessage = "";
        },
        error: (e) => {
          this.validateTokenError = true;
          if (e.error && e.error['message']) {
            this.validateTokenErrorMessage = e.error['message'];
          } else if(e.message) {
            this.validateTokenErrorMessage = e.message;
          } else {
            this.validateTokenErrorMessage = "Unexpected error. Please try again later.";
          }
        }
      });
    }

    model = new CompleteRegistrationRequest(this.token);

    onSubmit() {
      this.confirmRegistrationService.completeRegistration(this.organizationId, this.model).subscribe({
        next: (v) => this.router.navigate(['/login']),
        error: (e) => {
          this.confirmError = true;
          if (e.error && e.error['message']) {
            this.confirmErrorMessage = e.error['message'];
          } else if(e.message) {
            this.confirmErrorMessage = e.message;
          } else {
            this.confirmErrorMessage = "Unexpected error. Please try again later.";
          }
        } 
      });
    }
}
