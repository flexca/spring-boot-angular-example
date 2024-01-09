import { TableWrapperCellLinkModel } from "src/app/table-wrapper/table-wrapper-cell-link.model";

export class UserTableRowModel {

    constructor(public id?: TableWrapperCellLinkModel, public organizationId?: string, public firstName?: string, public lastName?: string, public email?: string, 
        public status?: string, public scope?: string, public permissions?: string[], public createdAt?: string) {  
    }
}