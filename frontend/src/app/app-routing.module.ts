import { NgModule } from '@angular/core';
import { RouterModule, Routes, mapToCanActivate } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { ConfirmRegistrationComponent } from './confirm-registration/confirm-registration.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { OrganizationComponent } from './organization/organization.component';
import { OrgSettingsComponent } from './organization/org-settings/org-settings.component';
import { OrgUsersComponent } from './organization/org-users/org-users.component';
import { OrgUsersAddComponent } from './organization/org-users/org-users-add/org-users-add.component';
import { OrgUsersDetailsComponent } from './organization/org-users/org-users-details/org-users-details.component';
import { OrgUsersEditComponent } from './organization/org-users/org-users-edit/org-users-edit.component';
import { AuthGuard } from './generic/auth.guard'
import { ConfirmUserRegistrationComponent } from './confirm-user-registration/confirm-user-registration.component';
import { ResetPasswordCompleteComponent } from './reset-password/reset-password-complete/reset-password-complete.component';
import { OrgSettingsEditComponent } from './organization/org-settings/org-settings-edit/org-settings-edit.component';

const organizationRoutes: Routes = [
  { path: 'settings', component: OrgSettingsComponent, canActivate: mapToCanActivate([AuthGuard]) },
  { path: 'settings/edit', component: OrgSettingsEditComponent, canActivate: mapToCanActivate([AuthGuard]) },
  { path: 'users', component: OrgUsersComponent, canActivate: mapToCanActivate([AuthGuard]) },
  { path: 'users/add', component: OrgUsersAddComponent, canActivate: mapToCanActivate([AuthGuard]) },
  { path: 'users/:userId/details', component: OrgUsersDetailsComponent, canActivate: mapToCanActivate([AuthGuard]) },
  { path: 'users/:userId/edit', component: OrgUsersEditComponent, canActivate: mapToCanActivate([AuthGuard]) }
];

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'confirm-registration/:token/organization/:organizationId', component: ConfirmRegistrationComponent },
  { path: 'confirm-registration/:token/user/:userId', component: ConfirmUserRegistrationComponent },
  { path: 'reset-password', component: ResetPasswordComponent },
  { path: 'reset-password/:token/user/:userId', component: ResetPasswordCompleteComponent },
  { path: 'organization', component: OrganizationComponent, canActivate: mapToCanActivate([AuthGuard]) },
  { path: 'organization/:organizationId', component: OrganizationComponent, canActivate: mapToCanActivate([AuthGuard]) },
  { path: 'organization/:organizationId', component: OrganizationComponent, children: organizationRoutes, canActivateChild: mapToCanActivate([AuthGuard]) },
  {path: '', redirectTo: 'organization', pathMatch: 'full'},
  {path: '**', redirectTo: 'organization'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
