package cc.worldmandia.web;

import cc.worldmandia.security.auth.AuthenticationServiceImpl;
import cc.worldmandia.security.auth.request.LogInRequest;
import cc.worldmandia.security.auth.request.SignUpRequest;
import cc.worldmandia.security.auth.response.JwtAuthenticationResponse;
import cc.worldmandia.security.config.CookieService;
import cc.worldmandia.user.UserRegisterDto;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    @Mock
    private CookieService cookieService;
    @Mock
    private AuthenticationServiceImpl authenticationService;
    @InjectMocks
    private RegistrationController registrationController;

    @Test
    void redirectToRegistrationForm() throws Exception {
        RegistrationController registrationController = new RegistrationController(authenticationService, cookieService);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();

        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("/registration.html"))
                .andExpect(model().attributeExists("userRegisterDto"))
                .andExpect(model().attribute("userRegisterDto", new UserRegisterDto()));
    }

    @Test
    void registeringUserSuccess() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("test@example.com");
        userRegisterDto.setUsername("testuser");
        userRegisterDto.setPassword("password");
        userRegisterDto.setRepeatedPassword("password");

        JwtAuthenticationResponse successResponse = new JwtAuthenticationResponse(200,"Registration successful", "token");
        when(authenticationService.signup(any(SignUpRequest.class))).thenReturn(successResponse);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        mockMvc.perform(post("/registration")
                        .flashAttr("user", userRegisterDto))
                .andExpect(status().isOk())
                .andExpect(view().name("registerSuccess"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void registeringUserPasswordMismatch() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("test@example.com");
        userRegisterDto.setUsername("testuser");
        userRegisterDto.setPassword("password");
        userRegisterDto.setRepeatedPassword("differentpassword");

        JwtAuthenticationResponse mismatchResponse = new JwtAuthenticationResponse(400,"Password mismatch", "token");
        when(authenticationService.signup(any(SignUpRequest.class))).thenReturn(mismatchResponse);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        mockMvc.perform(post("/registration")
                        .flashAttr("user", userRegisterDto))
                .andExpect(status().isOk())
                .andExpect(view().name("/registration.html"))
                .andExpect(model().attributeExists("userRegisterDto"))
                .andExpect(model().attribute("statusCode", "Password mismatch"));
    }

    @Test
    void redirectToLoginForm() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("test@example.com");
        userRegisterDto.setUsername("testuser");
        userRegisterDto.setPassword("password");
        userRegisterDto.setRepeatedPassword("password");

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login.html"))
                .andExpect(model().attributeExists("userRegisterDto"));
    }

    @Test
    void loginSuccess() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("test@example.com");
        userRegisterDto.setPassword("password");

        JwtAuthenticationResponse successResponse = new JwtAuthenticationResponse(200, "Login successful", "testToken");
        when(authenticationService.login(any(LogInRequest.class))).thenReturn(successResponse);

        Cookie expectedCookie = new Cookie("token", "testToken");
        when(cookieService.createCookie("token", "testToken")).thenReturn(expectedCookie);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        mockMvc.perform(post("/login")
                        .flashAttr("user", userRegisterDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/url-shortener-main"))
                .andExpect(cookie().exists("token"))
                .andExpect(cookie().value("token", "testToken"));
    }

    @Test
    void loginFailure() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setEmail("test@example.com");
        userRegisterDto.setPassword("incorrectPassword");

        JwtAuthenticationResponse failureResponse = new JwtAuthenticationResponse(401, "Invalid credentials", null);
        when(authenticationService.login(any(LogInRequest.class))).thenReturn(failureResponse);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        mockMvc.perform(post("/login")
                        .flashAttr("user", userRegisterDto))
                .andExpect(status().isOk())
                .andExpect(view().name("login.html"))
                .andExpect(model().attribute("statusCode", "Invalid credentials"))
                .andExpect(model().attributeExists("userRegisterDto"))
                .andExpect(cookie().doesNotExist("token"));
    }

    @Test
    void logout() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(unauthenticated());
    }
}