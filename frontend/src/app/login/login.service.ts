import { Injectable } from '@angular/core';
import { HttpClientService } from '../generic/httpclient.service';
import { LoginRequest } from './login.request'
import { LoginResponse } from './login.response';
import { Observable, Subject } from 'rxjs';
import { AuthenticationService } from '../generic/authentication.service';
import { RefreshTokenRequest } from './refreshtoken.request';

@Injectable({
  providedIn: 'root',
})
export class LoginService {

    REFRESH_INTERVAL_SECONDS = 600;
    refreshScheduled = false;

    constructor(private httpClientService: HttpClientService, private authenticationService: AuthenticationService) {
    }

    login(loginRequest: LoginRequest) : Observable<LoginResponse> {
        const result = new Subject<LoginResponse>();
        const body = JSON.stringify(loginRequest);
        this.httpClientService.post<LoginResponse>("/api/v1/authentication/login", body).subscribe({
          next: (response) => {
            const token: string = response.authenticationToken; 
            this.authenticationService.saveToken(token);
            const refreshToken: string = response.refreshToken;
            this.authenticationService.saveRefreshToken(refreshToken);
            if(!this.refreshScheduled) {
              this.refreshScheduled = true; 
              const timer = setInterval(() => {
                clearInterval(timer);
                this.refreshToken();
              }, this.REFRESH_INTERVAL_SECONDS * 1000);
            }
            result.next(response);
          },
          error: (e) => {
            result.error(e);
          },
          complete: () => {
            result.complete();
          } 
        });
        return result;
    }

    refreshToken(): void {
      const token = this.authenticationService.getRefreshToken();
      const request = new RefreshTokenRequest(token);
      const body = JSON.stringify(request);
      this.httpClientService.post<LoginResponse>("/api/v1/authentication/refresh-token", body).subscribe({
        next: (response) => {
          const token: string = response.authenticationToken; 
          this.authenticationService.saveToken(token);
          const timer = setInterval(() => {
            clearInterval(timer);
            this.refreshToken();
          }, this.REFRESH_INTERVAL_SECONDS * 1000);
        },
        error: (e) => {
          this.refreshScheduled = false;
        }
      });
    }
}
