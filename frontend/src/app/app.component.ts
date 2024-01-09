import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from './generic/authentication.service';
import { LoginService } from './login/login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  
  constructor(private authenticationService: AuthenticationService, private loginService: LoginService) {
  }

  ngOnInit() {
    if (this.authenticationService.isRefreshTokenAvailable() && !this.loginService.refreshScheduled) {
      this.loginService.refreshScheduled = true;
      this.loginService.refreshToken();
    }
  }
}
