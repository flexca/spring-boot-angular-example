import { Injectable } from "@angular/core";
import { HttpClientService } from "../generic/httpclient.service";
import { ResetPasswordRequest } from "./reset-password.request";
import { ValidateTokenRequest } from "../confirm-registration/validate-token.request";
import { CompleteUserRegistrationRequest } from "../confirm-user-registration/complete-user-registration.request";

@Injectable({
    providedIn: 'root',
})
export class ResetPasswordService {

    constructor(private httpClientService: HttpClientService) {
    }

    resetPassword(request: ResetPasswordRequest) {
        const body = JSON.stringify(request);
        return this.httpClientService.post("/api/v1/users/reset-password", body);
    }

    validateToken(id: string, request: ValidateTokenRequest) {
        const body = JSON.stringify(request);
        return this.httpClientService.post("/api/v1/users/" + id + "/reset-password-validate", body);
    }

    completeResetPassword(id: string, request: CompleteUserRegistrationRequest) {
        const body = JSON.stringify(request);
        return this.httpClientService.post("/api/v1/users/" + id + "/reset-password-complete", body);
    }
}
