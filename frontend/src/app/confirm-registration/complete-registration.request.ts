export class CompleteRegistrationRequest {

    constructor(public token: string, public adminPassword?: string, public adminPasswordConfirmation?: string) {
    }  
}
