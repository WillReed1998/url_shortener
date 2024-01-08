package cc.worldmandia.web;

import cc.worldmandia.security.auth.AuthenticationServiceImpl;
import cc.worldmandia.security.auth.request.LogInRequest;
import cc.worldmandia.security.auth.request.SignUpRequest;
import cc.worldmandia.security.auth.response.JwtAuthenticationResponse;
import cc.worldmandia.security.config.CookieService;
import cc.worldmandia.user.UserRegisterDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static cc.worldmandia.web.MainController.start;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class RegistrationController {

    private final AuthenticationServiceImpl authenticationService;
    private final CookieService cookieService;


    @GetMapping("/registration")
    public String redirectToRegistrationForm(Model model) {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        model.addAttribute(userRegisterDto);
        return "/registration";
    }

    @PostMapping("/registration")
    public String registeringUser(@ModelAttribute("user") @Valid UserRegisterDto userRegisterDto, Model model) {

        JwtAuthenticationResponse response = authenticationService.signup(new SignUpRequest(
                userRegisterDto.getEmail(),
                userRegisterDto.getUsername(),
                userRegisterDto.getPassword(),
                userRegisterDto.getRepeatedPassword()
        ));
        if (!userRegisterDto.getPassword().equals(userRegisterDto.getRepeatedPassword())) {
            model.addAttribute("userRegisterDto", userRegisterDto);
            model.addAttribute("statusCode", response.getMessage());
            return "/registration";
        }
        if (response.getStatus() == 200) {
            model.addAttribute("user", userRegisterDto);
            return "registerSuccess";
        }

        model.addAttribute("statusCode", response.getMessage());
        model.addAttribute("userRegisterDto", userRegisterDto);
        return "/registration";
    }

    @GetMapping("/login")
    public String redirectToLoginForm(Model model) {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        model.addAttribute(userRegisterDto);
        return "/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") @Valid UserRegisterDto userRegisterDto, Model model, HttpServletResponse httpResponse) {
        JwtAuthenticationResponse response = authenticationService.login(new LogInRequest(
                userRegisterDto.getEmail(),
                userRegisterDto.getPassword()
        ));
        if (response.getStatus() == 200) {
            model.addAttribute("token", response.getToken());
            Cookie tokenCookie = cookieService.createCookie("token", response.getToken());
            httpResponse.addCookie(tokenCookie);
            return "redirect:/url-shortener-main";
        }
        model.addAttribute("statusCode", response.getMessage());
        model.addAttribute("userRegisterDto", userRegisterDto);
        return "login";
    }
    @GetMapping ("/logout")
    public String logout (){
        SecurityContextHolder.getContext().setAuthentication(null);
        return "redirect:/login";
    }
}
