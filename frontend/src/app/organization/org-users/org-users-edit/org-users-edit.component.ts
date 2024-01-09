import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../user.service';
import { User } from 'src/app/generic/user.model';
import { UserCreateModel } from '../user-create.model';
import { AuthenticationService } from 'src/app/generic/authentication.service';
import { InfoMessageType } from 'src/app/info-message/info-message.component';

@Component({
  selector: 'app-org-users-edit',
  templateUrl: './org-users-edit.component.html',
  styleUrls: ['./org-users-edit.component.scss']
})
export class OrgUsersEditComponent {

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) { 
  }
  
  userId = this.route.snapshot.params['userId'];
  organizationId: string = "";
  
  fetchUserError = false;
  fetchUserErrorMessage = "";

  updateUserError = false;
  updateUserErrorMessage = "";
  
  errorMessageType = InfoMessageType.info;

  user: User | undefined = undefined;

  model = new UserCreateModel();
  
  ngOnInit(): void {
    this.userService.getUser(this.userId).subscribe({
      next: (response: User) => {
        this.fetchUserError = false;
        this.user = response;
        this.model.email = response.email;
        this.model.firstName = response.firstName;
        this.model.lastName = response.lastName;
        if(response.permissions?.includes(AuthenticationService.PERMISSION_ORGANIZATION_VIEW)) {
          this.model.permissionViewOrganization = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_ORGANIZATION_MANAGE)) {
          this.model.permissionManageOrganization = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_USER_VIEW)) {
          this.model.permissionViewUsers = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_USER_MANAGE)) {
          this.model.permissionManageUsers = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_ROOTCA_VIEW)) {
          this.model.permissionViewRootCA = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_ROOTCA_MANAGE)) {
          this.model.permissionManageRootCA = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_ICA_VIEW)) {
          this.model.permissionViewIntermidiateCA = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_ICA_MANAGE)) {
          this.model.permissionManageIntermidiateCA = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_END_ENTITY_VIEW)) {
          this.model.permissionViewEndEntity = true;
        }
        if(response.permissions?.includes(AuthenticationService.PERMISSION_END_ENTITY_MANAGE)) {
          this.model.permissionManageEndEntity = true;
        }
      },
      error: (e) => {
        this.fetchUserError = true;
        if (e.error) {
          this.fetchUserErrorMessage = e.error['message'];
        } else {
          this.fetchUserErrorMessage = "Unexpected error. Please try again later.";
        }
      }
    });
  }

  onSubmit() {
    this.userService.updateUser(this.userId, this.model).subscribe({
      next: (response: User) => {
        const detailsLink = "/organization/" + this.organizationId + "/users/" + this.userId + "/details"
        this.router.navigate([detailsLink]);
      },
      error: (e) => {
        this.updateUserError = true;
        if (e.error) {
          this.updateUserErrorMessage = e.error['message'];
        } else {
          this.updateUserErrorMessage = "Unexpected error. Please try again later.";
        }
      }
    });
  }

  cancel() {
    const detailsLink = "/organization/" + this.organizationId + "/users/" + this.userId + "/details";
    this.router.navigate([detailsLink]);
  }
}
