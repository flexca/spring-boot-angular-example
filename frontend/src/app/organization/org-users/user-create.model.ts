export class UserCreateModel {

    constructor(public organizationId?: string, public firstName?: string, public lastName?: string, public email?: string, 
        public permissionViewOrganization?: boolean, public permissionManageOrganization?: boolean,
        public permissionViewUsers?: boolean, public permissionManageUsers?: boolean,
        public permissionViewRootCA?: boolean, public permissionManageRootCA?: boolean,
        public permissionViewIntermidiateCA?: boolean, public permissionManageIntermidiateCA?: boolean,
        public permissionViewEndEntity?: boolean, public permissionManageEndEntity?: boolean,
        public permissionViewCertificateStructure?: boolean, public permissionManageCertificateStructure?: boolean,
        public permissionViewCertificateProfile?: boolean, public permissionManageCertificateProfile?: boolean
        ) {  
    }
}
