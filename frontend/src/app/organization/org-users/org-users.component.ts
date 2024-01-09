import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../user.service';
import { SearchResponse } from 'src/app/generic/search.response';
import { User } from 'src/app/generic/user.model';
import { UserSearchRequest } from '../user-search.request';
import { TableWrapperColumnModel } from 'src/app/table-wrapper/table-wrapper-column.model';
import { TableWrapperFilterModel } from 'src/app/table-wrapper/table-wrapper-filter.model';
import { TableWrapperFilterType } from 'src/app/table-wrapper/table-wrapper-filter.type';
import { TableWrapperFilterStateModel } from 'src/app/table-wrapper/table-wrapper-filter-state.model';
import { UserTableRowModel } from './org-user-table-row.model';
import { TableWrapperCellLinkModel } from 'src/app/table-wrapper/table-wrapper-cell-link.model';

@Component({
  selector: 'app-org-users',
  templateUrl: './org-users.component.html',
  styleUrls: ['./org-users.component.scss']
})
export class OrgUsersComponent implements OnInit {
  
  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService) { 
  }

  organizationId: string = "";
  usersSearchResult: SearchResponse<User> | null = null;
  offset = 0;
  pageSize = 10;
  nextPageAvailable = false;

  columns: Array<TableWrapperColumnModel> = [
    {'id': 'id', 'title': 'ID', 'sorting': false, 'link': true},
    {'id': 'email', 'title': 'Email', 'sorting': true, 'link': false},
    {'id': 'firstName', 'title': 'First name', 'sorting': false, 'link': false},
    {'id': 'lastName', 'title': 'Last name', 'sorting': false, 'link': false},
    {'id': 'createdAt', 'title': 'Created', 'sorting': true, 'link': false},
    {'id': 'status', 'title': 'Status', 'sorting': false, 'link': false}
  ];

  displayedColumns: Array<string> = ['id', 'email', 'firstName', 'lastName', 'createdAt', 'status'];

  filterModel: Array<TableWrapperFilterModel> = [
    {'id': 'id', 'title': 'ID', 'type': TableWrapperFilterType.text},
    {'id': 'email', 'title': 'Email', 'type': TableWrapperFilterType.text},
    {'id': 'name', 'title': 'Name', 'type': TableWrapperFilterType.text},
    {'id': 'createdAt', 'title': 'Created', 'type': TableWrapperFilterType.date},
    {'id': 'status', 'title': 'Status', 'type': TableWrapperFilterType.fixed, 'filterData': [
      {'id': 'active', 'title': 'Active'}, 
      {'id': 'pending', 'title':'Pending'}, 
      {'id': 'disabled', 'title':'Disabled'}
    ]}
  ];

  tableDataModel: Array<UserTableRowModel> = [];

  ngOnInit() {
    this.loadUsers();
  }

  addUser() {
    const link = "/organization/" + this.organizationId + "/users/add";
    console.log("add user: " + link);
    this.router.navigate([link]);
  }

  nextPage() {
    if(this.usersSearchResult && this.usersSearchResult.nextPage) {
      this.offset += this.pageSize;
      this.loadUsers();
    }
  }

  previousPage() {
    if(this.offset > 0) {
      let newOffset = this.offset - this.pageSize;
      if(newOffset < 0) {
        newOffset = 0;
      }
      this.offset = newOffset;
      this.loadUsers();
    }
  }

  filtersChanged(filtersState: TableWrapperFilterStateModel) {
    this.offset = 0;
    this.loadUsers(filtersState);
  }

  loadUsers(filtersState?: TableWrapperFilterStateModel) {
    const searchRequest = new UserSearchRequest(this.organizationId, this.pageSize, this.offset);
    if(filtersState) {
      const idFilter = filtersState.findFilter("id");
      if(idFilter) {
          searchRequest.id = idFilter.value;
      }
      const emailFilter = filtersState.findFilter("email");
      if(emailFilter) {
          searchRequest.email = emailFilter.value;
      }
      const nameFilter = filtersState.findFilter("name");
      if(nameFilter) {
          searchRequest.name = nameFilter.value;
      }
      const createdFilter = filtersState.findFilter("createdAt");
      if(createdFilter) {
        searchRequest.createdFrom = createdFilter.value['startDate'];
        searchRequest.createdTo = createdFilter.value['endDate'];
      }
      const statusFilter = filtersState.findFilter("status");
      if(statusFilter) {
        searchRequest.status = statusFilter.value;
      }
    }
    this.userService.search(searchRequest).subscribe({
      next: (response: SearchResponse<User>) => {
        this.usersSearchResult = response;
        this.nextPageAvailable = response.nextPage;
        const tableData: Array<UserTableRowModel> = [];
        for(let user of response.items) {
          if(user.id) {
            const userRow = new UserTableRowModel();
            const routerLink: Array<string> = [user.id, "details"];
            const idLink = new TableWrapperCellLinkModel(user.id, routerLink);
            userRow.id = idLink;
            userRow.email = user.email;
            userRow.firstName = user.firstName;
            userRow.lastName = user.lastName;
            userRow.createdAt = user.createdAt;
            userRow.status = user.status;
            tableData.push(userRow);
          }
        }
        this.tableDataModel = tableData;
      },
      error: (e) => {
        //this.offset = 0;
        //this.nextPageAvailable = false;
      }
    });
  }
}
