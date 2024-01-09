import { Component, OnInit } from '@angular/core';
import { Organization } from 'src/app/generic/organization.model';
import { OrganizationService } from '../organization.service';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'src/app/confirmation-modal/confirmation-modal.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-org-settings',
  templateUrl: './org-settings.component.html',
  styleUrls: ['./org-settings.component.scss']
})
export class OrgSettingsComponent implements OnInit {

  constructor(private organizationService: OrganizationService, private router: Router, private modalService: NgbModal, public dialog: MatDialog) {
  }

  organizationId: string = "";

  fetchOrganizationError = false;
  fetchOrganizationErrorMessage = "";

  updateUserStatusError = false;
  updateUserStatusErrorMessage = "";
  
  model = new Organization();

  ngOnInit() {
    this.organizationService.getOrganization(this.organizationId).subscribe({
      next: (response: Organization) => {
        this.fetchOrganizationError = false;
        this.model = response;
      },
      error: (e) => {
        this.fetchOrganizationError = true;
        if (e.error) {
          this.fetchOrganizationErrorMessage = e.error['message'];
        } else {
          this.fetchOrganizationErrorMessage = "Unexpected error. Please try again later.";
        }
      }
    });
  }

  editOrgSettings(event: Event) {
    event.preventDefault();
    const link = "/organization/" + this.organizationId + "/settings/edit"
    this.router.navigate([link]);
  }

  showConfirmation(event: Event) {
    const dialogRef = this.dialog.open(ConfirmationModalComponent, {
      data: {},
    });

    if(this.model.status == 'active') {
      dialogRef.componentInstance.title = 'Confirm action';
      dialogRef.componentInstance.content = 'Are you sure you want to disable organization?';
      dialogRef.componentInstance.okButtonCaption = 'Disable';
    } else {
      dialogRef.componentInstance.title = 'Confirm action';
      dialogRef.componentInstance.content = 'Are you sure you want to enable organization?';
      dialogRef.componentInstance.okButtonCaption = 'Enable';
    }
    dialogRef.componentInstance.cancelButtonCaption = "Cancel";

    dialogRef.componentInstance.okClicked.subscribe(() => {
      const id = this.model.id;
      if(id) {
        let status: string = "";
        if(this.model.status == 'active') {
          status = 'disabled';
        } else if(this.model.status == 'disabled') {
          status = 'active';
        }
        this.organizationService.updateOrganizationStatus(id, status).subscribe({
          next: (response: Organization) => {
            this.updateUserStatusError = false;
            this.model = response;
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
      dialogRef.close();
    });
    dialogRef.componentInstance.cancelClicked.subscribe(() => {
      dialogRef.close();
    });
  }
  
  showConfirmation2(event: Event) {
    event.preventDefault();
    const modalRef = this.modalService.open(ConfirmationModalComponent);
    if(this.model.status == 'active') {
      modalRef.componentInstance.title = 'Confirm action';
      modalRef.componentInstance.content = 'Are you sure you want to disable organization?';
      modalRef.componentInstance.okButtonCaption = 'Disable';
    } else {
      modalRef.componentInstance.title = 'Confirm action';
      modalRef.componentInstance.content = 'Are you sure you want to enable organization?';
      modalRef.componentInstance.okButtonCaption = 'Enable';
    }
    modalRef.componentInstance.cancelButtonCaption = 'Cancel';
    modalRef.componentInstance.okClicked.subscribe(() => {
      const id = this.model.id;
      if(id) {
        let status: string = "";
        if(this.model.status == 'active') {
          status = 'disabled';
        } else if(this.model.status == 'disabled') {
          status = 'active';
        }
        this.organizationService.updateOrganizationStatus(id, status).subscribe({
          next: (response: Organization) => {
            this.updateUserStatusError = false;
            this.model = response;
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
