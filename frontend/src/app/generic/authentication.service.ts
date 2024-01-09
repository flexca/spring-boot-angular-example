import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Authentication } from './authentication.model';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

    static readonly TOKEN_STORAGE_NAME = "flexca-token";
    static readonly REFRESH_TOKEN_STORAGE_NAME = "flexca-refresh-token";

    static readonly PERMISSION_ORGANIZATION_VIEW   = "permission_organization_view";
    static readonly PERMISSION_ORGANIZATION_MANAGE = "permission_organization_manage";
  
    static readonly PERMISSION_USER_VIEW   = "permission_user_view";
    static readonly PERMISSION_USER_MANAGE = "permission_user_manage";
  
    static readonly PERMISSION_ROOTCA_VIEW   = "permission_rootca_view";
    static readonly PERMISSION_ROOTCA_MANAGE = "permission_rootca_manage";
  
    static readonly PERMISSION_ICA_VIEW   = "permission_ica_view";
    static readonly PERMISSION_ICA_MANAGE = "permission_ica_manage";
  
    static readonly PERMISSION_END_ENTITY_VIEW   = "permission_end_entity_view";
    static readonly PERMISSION_END_ENTITY_MANAGE = "permission_end_entity_manage";

    static readonly PERMISSION_CERTIFICATE_STRUCTURE_VIEW   = "permission_certificate_structure_view";
    static readonly PERMISSION_CERTIFICATE_STRUCTURE_MANAGE = "permission_certificate_structure_manage";

    static readonly PERMISSION_CERTIFICATE_PROFILE_VIEW   = "permission_certificate_profile_view";
    static readonly PERMISSION_CERTIFICATE_PROFILE_MANAGE = "permission_certificate_profile_manage";

    constructor(private jwtHelperService: JwtHelperService) {
    }

    public saveToken(token: string) {
      const authentication = this.jwtHelperService.decodeToken<Authentication>(token);
      localStorage.setItem(AuthenticationService.TOKEN_STORAGE_NAME, token);
    }

    public saveRefreshToken(refreshToken: string) {
      const authentication = this.jwtHelperService.decodeToken<Authentication>(refreshToken);
      localStorage.setItem(AuthenticationService.REFRESH_TOKEN_STORAGE_NAME, refreshToken);
    }

    public getAuthentication() : Authentication | null {
      const token = localStorage.getItem(AuthenticationService.TOKEN_STORAGE_NAME);
      if (token && token.length > 0) {
        return this.jwtHelperService.decodeToken<Authentication>(token);
      }
      return null; 
    }

    public getToken() : string | null {
      const token = localStorage.getItem(AuthenticationService.TOKEN_STORAGE_NAME);
      return token;
    }

    public getRefreshToken() : string | null {
      const refreshToken = localStorage.getItem(AuthenticationService.REFRESH_TOKEN_STORAGE_NAME);
      return refreshToken;
    }

    public isLoggedIn() : boolean {
      const token = localStorage.getItem(AuthenticationService.TOKEN_STORAGE_NAME);
      if (token && token.length > 0) {
        if (this.jwtHelperService.isTokenExpired(token)) {
          return false;
        }
        return true;
      }
      return false;
    }

    public isRefreshTokenAvailable() : boolean {
      const token = localStorage.getItem(AuthenticationService.REFRESH_TOKEN_STORAGE_NAME);
      if (token && token.length > 0) {
        if (this.jwtHelperService.isTokenExpired(token)) {
          return false;
        }
        return true;
      }
      return false;
    }

    public Logout() {
      localStorage.removeItem(AuthenticationService.TOKEN_STORAGE_NAME);
      localStorage.removeItem(AuthenticationService.REFRESH_TOKEN_STORAGE_NAME);
    }
}
