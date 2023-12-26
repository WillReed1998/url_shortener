package cc.worldmandia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class USWebSecurity {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizationRequestMatcher -> {
            authorizationRequestMatcher.requestMatchers("/api-docs/**").permitAll();
            authorizationRequestMatcher.requestMatchers("/swagger/**").permitAll();
            authorizationRequestMatcher.requestMatchers("/h2-console/**").permitAll();
            authorizationRequestMatcher.requestMatchers("/**").hasRole("USER");
        }).formLogin(Customizer.withDefaults());

        return http.build();
    }
}
