package com.github.flexca.sbae.backend.authentication.endpoints;

import com.github.flexca.sbae.backend.authentication.model.LoginRequest;
import com.github.flexca.sbae.backend.authentication.model.LoginResponse;
import com.github.flexca.sbae.backend.authentication.model.RefreshRequest;
import com.github.flexca.sbae.backend.authentication.model.RefreshResponse;
import com.github.flexca.sbae.backend.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/refresh-token")
    public RefreshResponse refresh(@RequestBody RefreshRequest request) {
        return authenticationService.refreshToken(request);
    }
}
