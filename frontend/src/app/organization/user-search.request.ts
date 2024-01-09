export class UserSearchRequest {

    constructor(
        public organizationId: string,
        public limit: number,
        public offset: number,
        public id?: string,
        public email?: string,
        public name?: string,
        public createdFrom?: string,
        public createdTo?: string,
        public status?: string
        ) {  

    }

}