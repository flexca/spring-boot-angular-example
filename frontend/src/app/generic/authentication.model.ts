import { User } from "./user.model";

export class Authentication {

    constructor(public user: User, public exp: number) {  
    }
  
}
