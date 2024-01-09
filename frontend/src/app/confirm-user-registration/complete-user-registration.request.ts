export class CompleteUserRegistrationRequest {

    constructor(public token: string, public userPassword?: string, public userPasswordConfirmation?: string) {
    }

    
}
