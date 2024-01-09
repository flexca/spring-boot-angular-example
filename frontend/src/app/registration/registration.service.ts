import { Injectable } from '@angular/core';
import { HttpClientService } from '../generic/httpclient.service';
import {RegistrationRequest} from './registrationform'

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {

    constructor(private httpClientService: HttpClientService) {  }

    registerOrganization(registrationRequest: RegistrationRequest) {
        const body=JSON.stringify(registrationRequest);
        return this.httpClientService.post("/api/v1/organizations/registration", body);
    }
}
