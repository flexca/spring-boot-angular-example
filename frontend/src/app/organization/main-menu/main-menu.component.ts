import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from 'src/app/generic/authentication.service';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.scss']
})
export class MainMenuComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private authenticationService: AuthenticationService) { 
  }

  organizationId = this.route.snapshot.params['organizationId'];
  selectedItem = "settings";

  ngOnInit() {
    if(!this.route.children || this.route.children.length == 0) {
      if(this.route.snapshot.url && this.route.snapshot.url.length == 2) {
        if(this.route.snapshot.url[1].path === this.organizationId) {
          this.router.navigate(['/organization', this.organizationId, this.selectedItem]);
        }
      }
    } else {
      if(this.route.children[0].snapshot && this.route.children[0].snapshot.url && this.route.children[0].snapshot.url.length > 0) {
        this.selectedItem = this.route.children[0].snapshot.url[0].path;
      }
    }
  }

  selectItem(itemId: string) {
    this.selectedItem = itemId;
  }
}
