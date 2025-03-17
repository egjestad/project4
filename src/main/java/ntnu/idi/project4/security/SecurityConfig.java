package ntnu.idi.project4.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Autowired
  private TokenUtil tokenUtil;

  public SecurityConfig(TokenUtil tokenUtil) {
    this.tokenUtil = tokenUtil;
  }


@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http.csrf(csrf -> csrf.disable())
          .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:5173"));
            config.setAllowedMethods(List.of("GET", "POST"));
            config.setAllowedHeaders(List.of("*"));
            return config;
          }))
          .authorizeHttpRequests(auth -> auth
                  .requestMatchers("/register", "/login").permitAll()
                  .anyRequest().authenticated()
          );
  return http.build();
}



  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}
