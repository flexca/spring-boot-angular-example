import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { RegistrationComponent } from './registration/registration.component';
import { ConfirmRegistrationComponent } from './confirm-registration/confirm-registration.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { JwtModule } from "@auth0/angular-jwt";
import { OrganizationComponent } from './organization/organization.component';
import { HeaderComponent } from './organization/header/header.component';
import { MainMenuComponent } from './organization/main-menu/main-menu.component';
import { OrgSettingsComponent } from './organization/org-settings/org-settings.component';
import { OrgUsersComponent } from './organization/org-users/org-users.component';
import { OrgUsersAddComponent } from './organization/org-users/org-users-add/org-users-add.component';
import { OrgUsersDetailsComponent } from './organization/org-users/org-users-details/org-users-details.component';
import { OrgUsersEditComponent } from './organization/org-users/org-users-edit/org-users-edit.component';
import { ConfirmUserRegistrationComponent } from './confirm-user-registration/confirm-user-registration.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { TextFieldModule } from '@angular/cdk/text-field';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatCardModule } from '@angular/material/card';
import { ResetPasswordCompleteComponent } from './reset-password/reset-password-complete/reset-password-complete.component';
import { OrgSettingsEditComponent } from './organization/org-settings/org-settings-edit/org-settings-edit.component';
import { ConfirmationModalComponent } from './confirmation-modal/confirmation-modal.component';
import { NgJsonEditorModule } from 'ang-jsoneditor';
import { NgSelectModule } from '@ng-select/ng-select';
import { InfoMessageComponent } from './info-message/info-message.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatChipsModule } from '@angular/material/chips';
import { TableWrapperComponent } from './table-wrapper/table-wrapper.component';
import { MomentDateAdapter, MAT_MOMENT_DATE_ADAPTER_OPTIONS } from '@angular/material-moment-adapter';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';

const SBAE_DATE_FORMATS = {
  parse: {
    dateInput: 'DD MMM YYYY',
  },
  display: {
    dateInput: 'DD MMM YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    ConfirmRegistrationComponent,
    ResetPasswordComponent,
    OrganizationComponent,
    HeaderComponent,
    MainMenuComponent,
    OrgSettingsComponent,
    OrgUsersComponent,
    OrgUsersAddComponent,
    OrgUsersDetailsComponent,
    OrgUsersEditComponent,
    ConfirmUserRegistrationComponent,
    ResetPasswordCompleteComponent,
    OrgSettingsEditComponent,
    ConfirmationModalComponent,
    InfoMessageComponent,
    TableWrapperComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgSelectModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    JwtModule.forRoot({
      config: {
        tokenGetter:  () => localStorage.getItem('flexca-token')
      }
    }),
    BrowserAnimationsModule,
    MatButtonModule,
    TextFieldModule,
    MatInputModule,
    MatFormFieldModule,
    MatCardModule,
    MatIconModule,
    MatGridListModule,
    ReactiveFormsModule,
    NgJsonEditorModule,
    MatMenuModule,
    MatDialogModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatChipsModule,
    MatCheckboxModule
  ],
  providers: [
    {
      provide: DateAdapter,
      useClass: MomentDateAdapter,
      deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS],
    },
    {provide: MAT_DATE_FORMATS, useValue: SBAE_DATE_FORMATS},
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
