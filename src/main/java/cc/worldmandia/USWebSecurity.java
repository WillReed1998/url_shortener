package cc.worldmandia;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class USWebSecurity {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.authorizeHttpRequests(authorizationRequestMatcher -> {
            authorizationRequestMatcher.requestMatchers("/v1/**").permitAll();
            authorizationRequestMatcher.requestMatchers("/api-docs/**").permitAll();
            authorizationRequestMatcher.requestMatchers("/swagger/**").permitAll();
            authorizationRequestMatcher.requestMatchers(PathRequest.toH2Console()).permitAll();
            authorizationRequestMatcher.requestMatchers("/**").hasRole("USER");
        }).formLogin(Customizer.withDefaults());

        return http.build();
    }
}
