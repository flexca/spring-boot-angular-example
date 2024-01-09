import { Injectable } from '@angular/core';
import { HttpClientService } from '../generic/httpclient.service';
import { ValidateTokenRequest } from './validate-token.request'
import { CompleteRegistrationRequest } from './complete-registration.request'

@Injectable({
  providedIn: 'root',
})
export class ConfirmRegistrationService {

    constructor(private httpClientService: HttpClientService) {  }

    validateToken(organizationId: string, request: ValidateTokenRequest) {
        const body=JSON.stringify(request);
        return this.httpClientService.post("/api/v1/organizations/" + organizationId + "/validate-token", body);
    }

    completeRegistration(organizationId: string, request: CompleteRegistrationRequest) {
        const body=JSON.stringify(request);
        return this.httpClientService.post("/api/v1/organizations/" + organizationId + "/confirm", body);
    }
}
