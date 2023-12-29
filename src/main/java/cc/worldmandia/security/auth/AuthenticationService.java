package cc.worldmandia.security.auth;

import cc.worldmandia.security.auth.dao.JwtAuthenticationResponse;
import cc.worldmandia.security.auth.dao.LogInRequest;
import cc.worldmandia.security.auth.dao.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse login(LogInRequest request);
}