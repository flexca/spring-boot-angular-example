import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../generic/authentication.service';
import { Authentication } from '../generic/authentication.model';

@Component({
  selector: 'app-organization',
  templateUrl: './organization.component.html',
  styleUrls: ['./organization.component.scss']
})
export class OrganizationComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private authenticationService: AuthenticationService) { 
  }

  organizationId = this.route.snapshot.params['organizationId'];

  ngOnInit() {
    const authentication = this.authenticationService.getAuthentication();
    if(authentication != null) {
      if(authentication.user.scope === 'organization-scope') {
        if(authentication.user.organizationId !== this.organizationId) {
          this.router.navigate(['/organization', authentication.user.organizationId]);
        }
      }
    }
  }

  onOutletLoaded(component: { [x: string]: any; }) {
    component['organizationId'] = this.organizationId;
  } 

}
