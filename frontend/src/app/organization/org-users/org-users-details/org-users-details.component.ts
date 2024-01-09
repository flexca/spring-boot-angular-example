import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../user.service';
import { User } from 'src/app/generic/user.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'src/app/confirmation-modal/confirmation-modal.component';
import { InfoMessageType } from 'src/app/info-message/info-message.component';

@Component({
  selector: 'app-org-users-details',
  templateUrl: './org-users-details.component.html',
  styleUrls: ['./org-users-details.component.scss']
})
export class OrgUsersDetailsComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private modalService: NgbModal) { 
  }
  
  userId = this.route.snapshot.params['userId'];
  organizationId: string = "";
  
  fetchUserError = false;
  fetchUserErrorMessage = "";

  updateUserStatusError = false;
  updateUserStatusErrorMessage = "";

  user: User = new User();

  displayConfirm = false;

  errorMessageType = InfoMessageType.info;
  
  ngOnInit(): void {
    this.userService.getUser(this.userId).subscribe({
      next: (response: User) => {
        this.fetchUserError = false;
        this.user = response;
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

  editUser(event: Event) {
    event.preventDefault();
    const editLink = "/organization/" + this.organizationId + "/users/" + this.userId + "/edit";
    this.router.navigate([editLink]);
  }

  showConfirmation(event: Event) {
    event.preventDefault();
    const modalRef = this.modalService.open(ConfirmationModalComponent);
    if(this.user.status == 'active') {
      modalRef.componentInstance.title = 'Confirm action';
      modalRef.componentInstance.content = 'Are you sure you want to disable user?';
      modalRef.componentInstance.okButtonCaption = 'Disable';
    } else {
      modalRef.componentInstance.title = 'Confirm action';
      modalRef.componentInstance.content = 'Are you sure you want to enable user?';
      modalRef.componentInstance.okButtonCaption = 'Enable';
    }
    modalRef.componentInstance.cancelButtonCaption = 'Cancel';
    modalRef.componentInstance.okClicked.subscribe(() => {
      const id = this.user.id;
      if(id) {
        let status: string = "";
        if(this.user.status == 'active') {
          status = 'disabled';
        } else if(this.user.status == 'disabled') {
          status = 'active';
        }
        this.userService.updateUserStatus(id, status).subscribe({
          next: (response: User) => {
            this.updateUserStatusError = false;
            this.user = response;
          },
          error: (e) => {
            this.updateUserStatusError = true;
            if (e.error) {
              this.updateUserStatusErrorMessage = e.error['message'];
            } else {
              this.updateUserStatusErrorMessage = "Unexpected error. Please try again later.";
            }
          }
        });
      }
      modalRef.close();
    });
    modalRef.componentInstance.cancelClicked.subscribe(() => {
      modalRef.close();
    });
  }
}
