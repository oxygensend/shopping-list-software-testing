package com.oxygensend.backend.application.auth;


import com.oxygensend.backend.application.auth.request.AuthenticationRequest;
import com.oxygensend.backend.application.auth.request.RefreshTokenRequest;
import com.oxygensend.backend.application.auth.response.AuthenticationResponse;
import com.oxygensend.backend.application.auth.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthenticationResponse register(
            @RequestBody @Validated RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/access_token")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse authenticate(
            @RequestBody @Validated AuthenticationRequest request
    ) {
        return authService.authenticate(request);
    }

    @PostMapping("/refresh_token")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse refreshToken(
            @RequestBody @Validated RefreshTokenRequest request
    ) {
        return authService.refreshToken(request);
    }

}
