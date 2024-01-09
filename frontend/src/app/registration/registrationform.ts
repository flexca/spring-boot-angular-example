export class RegistrationRequest {

constructor(
    public organizationName?: string,
    public organizationDescription?: string,
    public organizationAdminFirstName?: string,
    public organizationAdminLastName?: string,
    public organizationAdminEmail?: string
) {  }

}
