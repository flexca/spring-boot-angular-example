import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/generic/authentication.service';
import { HttpClientService } from 'src/app/generic/httpclient.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService, private httpClientService: HttpClientService, private router: Router) {

  }

  userFullName = "User";

  ngOnInit() {
    const authentication = this.authenticationService.getAuthentication();
    if(authentication) {
      this.userFullName = authentication.user.firstName + " " + authentication.user.lastName;
    }
  }

  Logout(event: Event) {
    event.preventDefault();
    this.authenticationService.Logout();
    this.httpClientService.post("/api/rest/users/logout", "").subscribe({
      next: (response) => {
        this.router.navigate(['/login']);
      },
      error: (e) => {
        this.router.navigate(['/login']);
      }
    });
  }

}
