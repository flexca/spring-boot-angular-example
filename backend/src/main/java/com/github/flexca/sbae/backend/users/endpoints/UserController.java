package com.github.flexca.sbae.backend.users.endpoints;

import com.github.flexca.sbae.backend.common.model.generic.UpdateStatusRequest;
import com.github.flexca.sbae.backend.common.model.search.SearchResponse;
import com.github.flexca.sbae.backend.users.model.CompleteUserRegistrationRequest;
import com.github.flexca.sbae.backend.users.model.UserCreateRequest;
import com.github.flexca.sbae.backend.users.model.UserDto;
import com.github.flexca.sbae.backend.users.model.UserResetPasswordRequest;
import com.github.flexca.sbae.backend.users.model.UserSearchRequest;
import com.github.flexca.sbae.backend.users.model.UserUpdateRequest;
import com.github.flexca.sbae.backend.users.model.UserValidateRegistrationTokenRequest;
import com.github.flexca.sbae.backend.users.model.UserValidateResetPasswordTokenRequest;
import com.github.flexca.sbae.backend.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("{id}/validate-token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateRegistrationToken(@PathVariable("id") String id, @RequestBody UserValidateRegistrationTokenRequest request) {
        userService.validateRegistrationToken(id, request);
    }

    @PostMapping("{id}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeRegistration(@PathVariable("id") String id, @RequestBody CompleteUserRegistrationRequest request) {
        userService.completeRegistration(id, request);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestResetPassword(@RequestBody UserResetPasswordRequest request) {
        userService.requestResetPassword(request);
    }

    @PostMapping("/{id}/reset-password-validate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void validateResetPasswordToken(@PathVariable("id") String id, @RequestBody UserValidateResetPasswordTokenRequest request) {
        userService.validateResetPasswordToken(id, request);
    }

    @PostMapping("/{id}/reset-password-complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeResetPassword(@PathVariable("id") String id, @RequestBody CompleteUserRegistrationRequest request) {
        userService.completeResetPassword(id, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@securityService.hasAnyOfPermission('PERMISSION_USER_VIEW', 'PERMISSION_USER_MANAGE')")
    public UserDto getById(@PathVariable("id") String id) {
        return userService.getById(id);
    }

    @GetMapping
    @PreAuthorize("@securityService.hasAnyOfPermission('PERMISSION_USER_VIEW', 'PERMISSION_USER_MANAGE')")
    public SearchResponse<UserDto> searchUsers(@ModelAttribute UserSearchRequest request) {
        return userService.search(request);
    }

    @PostMapping
    @PreAuthorize("@securityService.hasAnyOfPermission('PERMISSION_USER_MANAGE')")
    public UserDto create(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@securityService.hasAnyOfPermission('PERMISSION_USER_MANAGE')")
    public UserDto update(@PathVariable("id") String id, @RequestBody UserUpdateRequest request) {
        return userService.update(id, request);
    }

    @PutMapping("{id}/status")
    @PreAuthorize("@securityService.hasAnyOfPermission('PERMISSION_USER_MANAGE')")
    public UserDto updateStatus(@PathVariable("id") String id, @RequestBody UpdateStatusRequest updateStatusRequest) {
        return userService.updateStatus(id, updateStatusRequest);
    }
}
