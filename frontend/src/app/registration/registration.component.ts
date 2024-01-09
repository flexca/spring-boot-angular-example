import { Component, OnInit } from '@angular/core';
import {RegistrationRequest} from './registrationform'
import {RegistrationService} from './registration.service'
import {Router} from "@angular/router"
import { InfoMessageType } from '../info-message/info-message.component';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

    model = new RegistrationRequest();
    registrationDone = false;
    registrationError = false;
    registrationErrorMessage = "";
    errorMessageType = InfoMessageType.info;
    
    constructor(private registrationService: RegistrationService, private router: Router) {
    }

    ngOnInit() {
        this.registrationDone = false;
    }

    onSubmit() {
        this.registrationService.registerOrganization(this.model).subscribe({
          next: (v) => {
            this.registrationDone = true;
            this.registrationError = false;
            this.registrationErrorMessage = "";
          },
          error: (e) => {
            this.registrationError = true;
            if (e.error) {
              this.registrationErrorMessage = e.error['message'];
            } else {
              this.registrationErrorMessage = "Unexpected error. Please try again later.";
            }
          }
        });
    }

    cancel() {
      this.router.navigate(['/login'])
    }
}
