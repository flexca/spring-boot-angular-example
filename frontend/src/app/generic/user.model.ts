export class User {

    constructor(public id?: string, public organizationId?: string, public firstName?: string, public lastName?: string, public email?: string, 
        public status?: string, public scope?: string, public permissions?: string[], public createdAt?: string) {  
    }
}
