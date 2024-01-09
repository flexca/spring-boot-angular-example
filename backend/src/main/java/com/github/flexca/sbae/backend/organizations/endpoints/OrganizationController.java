package com.github.flexca.sbae.backend.organizations.endpoints;

import com.github.flexca.sbae.backend.common.model.generic.UpdateStatusRequest;
import com.github.flexca.sbae.backend.organizations.model.OrganizationCompleteRegistrationRequest;
import com.github.flexca.sbae.backend.organizations.model.OrganizationDto;
import com.github.flexca.sbae.backend.organizations.model.OrganizationRegistrationRequest;
import com.github.flexca.sbae.backend.organizations.model.OrganizationUpdateRequest;
import com.github.flexca.sbae.backend.organizations.model.OrganizationValidateRegistrationTokenRequest;
import lombok.RequiredArgsConstructor;
import com.github.flexca.sbae.backend.organizations.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
@CrossOrigin
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/registration")
    public OrganizationDto requestRegistration(@RequestBody OrganizationRegistrationRequest request) {
        return organizationService.requestRegistration(request);
    }

    @PostMapping("{id}/validate-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateRegistrationToken(@PathVariable("id") String id, @RequestBody OrganizationValidateRegistrationTokenRequest request) {
        organizationService.validateRegistrationToken(id, request);
    }

    @PostMapping("{id}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeRegistration(@PathVariable("id") String id, @RequestBody OrganizationCompleteRegistrationRequest request) {
        organizationService.completeRegistration(id, request);
    }

    @GetMapping("{id}")
    @PreAuthorize("@securityService.hasAnyOfPermission('PERMISSION_ORGANIZATION_VIEW', 'PERMISSION_ORGANIZATION_MANAGE')")
    public OrganizationDto getById(@PathVariable("id") String id) {
        return organizationService.getById(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("@securityService.hasAnyOfPermission('PERMISSION_ORGANIZATION_MANAGE')")
    public OrganizationDto update(@PathVariable("id") String id, @RequestBody OrganizationUpdateRequest request) {
        return organizationService.update(id, request);
    }

    @PutMapping("{id}/status")
    @PreAuthorize("@securityService.hasAnyOfPermission('PERMISSION_ORGANIZATION_MANAGE')")
    public OrganizationDto updateStatus(@PathVariable("id") String id, @RequestBody UpdateStatusRequest updateStatusRequest) {
        return organizationService.updateStatus(id, updateStatusRequest);
    }

}
