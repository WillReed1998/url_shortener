package cc.worldmandia.security.auth;

import cc.worldmandia.security.auth.response.JwtAuthenticationResponse;
import cc.worldmandia.security.auth.request.LogInRequest;
import cc.worldmandia.security.auth.request.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse login(LogInRequest request);
}