import { Injectable } from "@angular/core";
import { HttpClientService } from "../generic/httpclient.service";
import { HttpParams } from "@angular/common/http";
import { Organization } from "../generic/organization.model";
import { Observable } from "rxjs";
import { UserUpdateStatusRequest } from "./org-users/user-update-status.request";

@Injectable({
    providedIn: 'root'
})
export class OrganizationService {

    constructor(private httpClientService: HttpClientService) {
    }

    getOrganization(id: string) : Observable<Organization> {
        let params: HttpParams = new HttpParams();
        return this.httpClientService.get("/api/v1/organizations/" + id, params);
    }

    updateOrganization(id: string, model: Organization) : Observable<Organization> {
        const body = JSON.stringify(model);
        return this.httpClientService.put<Organization>("/api/v1/organizations/" + id, body);
    }

    updateOrganizationStatus(id: string, status: string) : Observable<Organization> {
        const body = JSON.stringify(new UserUpdateStatusRequest(status));
        return this.httpClientService.put("/api/v1/organizations/" + id + "/status", body);
    }

}