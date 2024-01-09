import { Injectable } from '@angular/core';
import { HttpClientService } from '../generic/httpclient.service';
import { ValidateTokenRequest } from '../confirm-registration/validate-token.request'
import { CompleteUserRegistrationRequest } from './complete-user-registration.request'

@Injectable({
  providedIn: 'root',
})
export class ConfirmUserRegistrationService {

    constructor(private httpClientService: HttpClientService) {  }

    validateToken(userId: string, request: ValidateTokenRequest) {
        const body=JSON.stringify(request);
        return this.httpClientService.post("/api/v1/users/" + userId + "/validate-token", body);
    }

    completeRegistration(userId: string, request: CompleteUserRegistrationRequest) {
        const body=JSON.stringify(request);
        return this.httpClientService.post("/api/v1/users/" + userId + "/confirm", body);
    }
}