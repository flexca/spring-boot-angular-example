import { Component, OnInit } from '@angular/core';
import { Organization } from 'src/app/generic/organization.model';
import { OrganizationService } from '../../organization.service';
import { Router } from '@angular/router';
import { InfoMessageType } from 'src/app/info-message/info-message.component';

@Component({
  selector: 'app-org-settings-edit',
  templateUrl: './org-settings-edit.component.html',
  styleUrls: ['./org-settings-edit.component.scss']
})
export class OrgSettingsEditComponent implements OnInit {

  constructor(private organizationService: OrganizationService, private router: Router) {
  }

  organizationId: string = "";

  fetchOrganizationError = false;
  fetchOrganizationErrorMessage = "";

  updateOrganizationError = false;
  updateOrganizationErrorMessage = "";
  
  errorMessageType = InfoMessageType.info;
  
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

  onSubmit() {
    this.organizationService.updateOrganization(this.organizationId, this.model).subscribe({
      next: (response: Organization) => {
        const detailsLink = "/organization/" + this.organizationId + "/settings"
        this.router.navigate([detailsLink]);
      },
      error: (e) => {
        this.updateOrganizationError = true;
        if (e.error) {
          this.updateOrganizationErrorMessage = e.error['message'];
        } else {
          this.updateOrganizationErrorMessage = "Unexpected error. Please try again later.";
        }
      }
    });
  }

  cancel() {
    const link = "/organization/" + this.organizationId + "/settings";
    this.router.navigate([link]);
  }
}
