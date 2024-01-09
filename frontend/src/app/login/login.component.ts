import { Component, OnInit } from '@angular/core';
import { LoginRequest } from './login.request'
import { LoginService } from './login.service'
import { AuthenticationService } from '../generic/authentication.service';

import {Router} from "@angular/router"
import { InfoMessageType } from '../info-message/info-message.component';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit {

    loginError = false;
    loginErrorMessage = "";
    hide = true;
    errorMessageType = InfoMessageType.info;

    constructor(private loginService: LoginService, private router: Router, private authenticationService: AuthenticationService) {
    }

    ngOnInit() {
    }

    model = new LoginRequest();

    onSubmit() {
        this.loginService.login(this.model).subscribe({
          next: (response) => {
            const token: string = response.authenticationToken; 
            this.authenticationService.saveToken(token);
            this.loginError = false;
            this.loginErrorMessage = "";
            this.router.navigate(['/'])
          },
          error: (e) => {
            this.loginError = true;
            if (e.error && e.error['message']) {
                this.loginErrorMessage = e.error['message'];
            } else if(e.message) {
              this.loginErrorMessage = e.message;
            } else {
                this.loginErrorMessage = "Unexpected error. Please try again later.";
            }
          },
          complete: () => {
          } 
        });
    }
}
