package cc.worldmandia.security.auth;

import cc.worldmandia.security.auth.response.JwtAuthenticationResponse;
import cc.worldmandia.security.auth.request.LogInRequest;
import cc.worldmandia.security.auth.request.SignUpRequest;
import cc.worldmandia.security.auth.util.AppMessages;
import cc.worldmandia.security.jwt.JwtServiceImpl;
import cc.worldmandia.user.User;
import cc.worldmandia.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static cc.worldmandia.security.auth.util.AppMessages.SUCCESS_MESSAGE;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    public static final int PASSWORD_REQUIRED_LENGTH = 8;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JwtAuthenticationResponse(HttpStatus.BAD_REQUEST.value(), AppMessages.USERNAME_ALREADY_EXISTS_MESSAGE, null)).getBody();
        }
        if (!isValidPassword(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JwtAuthenticationResponse(HttpStatus.BAD_REQUEST.value(), AppMessages.INVALID_PASSWORD_MESSAGE, null)).getBody();
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new JwtAuthenticationResponse(HttpStatus.BAD_REQUEST.value(), AppMessages.PASSWORD_MISMATCH_MESSAGE, null)).getBody();
        }
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        var jwt = jwtService.generateToken(new CustomUserDetails(newUser));
        newUser.setToken(jwt);
        userRepository.save(newUser);
        return JwtAuthenticationResponse.builder()
                .status(HttpStatus.OK.value())
                .message(SUCCESS_MESSAGE)
                .token(jwt)
                .build();
    }

    @Override
    public JwtAuthenticationResponse login(LogInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            var user = customUserDetailsService.loadUserByUsername(request.getUsername());
            var jwt = jwtService.generateToken(user);

            return ResponseEntity.ok()
                    .body(JwtAuthenticationResponse.builder()
                            .status(HttpStatus.OK.value())
                            .message(SUCCESS_MESSAGE)
                            .token(jwt)
                            .build()).getBody();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtAuthenticationResponse(HttpStatus.UNAUTHORIZED.value(), AppMessages.INVALID_CREDENTIALS_MESSAGE, null)).getBody();
        }

    }

    public static boolean isValidPassword(String password) {
        return (password.length() >= PASSWORD_REQUIRED_LENGTH)
                && (password.replaceAll("\\d", "").length() != password.length())
                && (!password.toLowerCase().equals(password))
                && (!password.toUpperCase().equals(password));
    }
}
