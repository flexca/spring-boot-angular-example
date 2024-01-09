import { Component } from '@angular/core';
import { UserCreateModel } from '../user-create.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../user.service';
import { User } from 'src/app/generic/user.model';
import { InfoMessageType } from 'src/app/info-message/info-message.component';

@Component({
  selector: 'app-org-users-add',
  templateUrl: './org-users-add.component.html',
  styleUrls: ['./org-users-add.component.scss']
})
export class OrgUsersAddComponent {

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) { 
  }
  
  organizationId: string = "";

  addUserError = false;
  addUserErrorMessage = "";

  errorMessageType = InfoMessageType.info;
  
  model = new UserCreateModel();

  onSubmit() {
    this.model.organizationId = this.organizationId;
    this.userService.addNewUser(this.model).subscribe({
      next: (response: User) => {
        const detailsLink = "/organization/" + this.organizationId + "/users/" + response.id + "/details";
        this.router.navigate([detailsLink]);
      },
      error: (e) => {
        this.addUserError = true;
        if (e.error) {
          this.addUserErrorMessage = e.error['message'];
        } else {
          this.addUserErrorMessage = "Unexpected error. Please try again later.";
        }
      }
    });
  }

  cancel() {
    const link = "/organization/" + this.organizationId + "/users";
    this.router.navigate([link]);
  }
}
