export class UserCreateRequest {

    constructor(public organizationId?: string, public firstName?: string, public lastName?: string, public email?: string, 
        public permissions?: string[]) {  
    }
}