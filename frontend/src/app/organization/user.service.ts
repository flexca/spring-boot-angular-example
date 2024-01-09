import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SearchResponse } from '../generic/search.response';
import { User } from '../generic/user.model';
import { HttpClientService } from '../generic/httpclient.service';
import { UserSearchRequest } from './user-search.request';
import { HttpParams } from '@angular/common/http';
import { UserCreateModel } from './org-users/user-create.model';
import { AuthenticationService } from '../generic/authentication.service';
import { UserCreateRequest } from './org-users/user-create.request';
import { UserUpdateStatusRequest } from './org-users/user-update-status.request';

@Injectable({
  providedIn: 'root'
})
export class UserService {

    constructor(private httpClientService: HttpClientService) {
    }

    search(searchRequest: UserSearchRequest) : Observable<SearchResponse<User>> {
      let params: HttpParams = new HttpParams();
      params = params.set("organizationId", searchRequest.organizationId);
      params = params.set("limit", searchRequest.limit);
      params = params.set("offset", searchRequest.offset);
      if(searchRequest.id != undefined) {
        params = params.set("id", searchRequest.id);
      }
      if(searchRequest.email != undefined) {
        params = params.set("email", searchRequest.email);
      }
      if(searchRequest.name != undefined) {
        params = params.set("name", searchRequest.name);
      }
      if(searchRequest.createdFrom != undefined) {
        params = params.set("createdFrom", searchRequest.createdFrom);
      }
      if(searchRequest.createdTo != undefined) {
        params = params.set("createdTo", searchRequest.createdTo);
      }
      if(searchRequest.status != undefined) {
        params = params.set("status", searchRequest.status);
      }
      return this.httpClientService.get("/api/v1/users", params);
    }

    getUser(id: string) : Observable<User> {
      let params: HttpParams = new HttpParams();
      return this.httpClientService.get("/api/v1/users/" + id, params);
    }

    addNewUser(userModel: UserCreateModel): Observable<User> {
      const permissions = this.modelToPermissions(userModel);
      const userCreateRequest: UserCreateRequest = new UserCreateRequest(userModel.organizationId, userModel.firstName, userModel.lastName, userModel.email, permissions);
      const body = JSON.stringify(userCreateRequest);
      return this.httpClientService.post<User>("/api/v1/users", body);
    }

    updateUser(id: string, userModel: UserCreateModel): Observable<User> {
      const permissions = this.modelToPermissions(userModel);
      const userCreateRequest: UserCreateRequest = new UserCreateRequest(userModel.organizationId, userModel.firstName, userModel.lastName, userModel.email, permissions);
      const body = JSON.stringify(userCreateRequest);
      return this.httpClientService.put<User>("/api/v1/users/" + id, body);
    }

    updateUserStatus(id: string, status: string): Observable<User> {
      const body = JSON.stringify(new UserUpdateStatusRequest(status));
      return this.httpClientService.put("/api/v1/users/" + id + "/status", body);
    }

    private modelToPermissions(userModel: UserCreateModel): string[] {
      const permissions: string[] = [];
      if(userModel.permissionViewOrganization) {
        permissions.push(AuthenticationService.PERMISSION_ORGANIZATION_VIEW);
      }
      if(userModel.permissionManageOrganization) {
        permissions.push(AuthenticationService.PERMISSION_ORGANIZATION_MANAGE);
      }
      if(userModel.permissionViewUsers) {
        permissions.push(AuthenticationService.PERMISSION_USER_VIEW);
      }
      if(userModel.permissionManageUsers) {
        permissions.push(AuthenticationService.PERMISSION_USER_MANAGE);
      }
      if(userModel.permissionViewRootCA) {
        permissions.push(AuthenticationService.PERMISSION_ROOTCA_VIEW);
      }
      if(userModel.permissionManageRootCA) {
        permissions.push(AuthenticationService.PERMISSION_ROOTCA_MANAGE);
      }
      if(userModel.permissionViewIntermidiateCA) {
        permissions.push(AuthenticationService.PERMISSION_ICA_VIEW);
      }
      if(userModel.permissionManageIntermidiateCA) {
        permissions.push(AuthenticationService.PERMISSION_ICA_MANAGE);
      }
      if(userModel.permissionViewEndEntity) {
        permissions.push(AuthenticationService.PERMISSION_END_ENTITY_VIEW);
      }
      if(userModel.permissionManageEndEntity) {
        permissions.push(AuthenticationService.PERMISSION_END_ENTITY_MANAGE);
      }
      if(userModel.permissionViewCertificateStructure) {
        permissions.push(AuthenticationService.PERMISSION_CERTIFICATE_STRUCTURE_VIEW);
      }
      if(userModel.permissionManageCertificateStructure) {
        permissions.push(AuthenticationService.PERMISSION_CERTIFICATE_STRUCTURE_MANAGE);
      }
      if(userModel.permissionViewCertificateProfile) {
        permissions.push(AuthenticationService.PERMISSION_CERTIFICATE_PROFILE_VIEW);
      }
      if(userModel.permissionManageCertificateProfile) {
        permissions.push(AuthenticationService.PERMISSION_CERTIFICATE_PROFILE_MANAGE);
      }
      return permissions;
    }
}
