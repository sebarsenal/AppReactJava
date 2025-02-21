package seba.java.cursoJava.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import seba.java.cursoJava.services.UserServiceInterface;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final UserServiceInterface userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String[] PUBLIC_POST_MATCHERS = { "/users" };
    private static final String[] PUBLIC_GET_MATCHERS = { "/posts/last", "/posts/{id}" };

    public WebSecurity(UserServiceInterface userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeRequests(authorize -> authorize
            .antMatchers(HttpMethod.POST, PUBLIC_POST_MATCHERS).permitAll()
            .antMatchers(HttpMethod.GET, PUBLIC_GET_MATCHERS).permitAll()
            .anyRequest().authenticated()
            )
            .addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new AuthorizationFilter(authenticationManager(http)), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userService)
            .passwordEncoder(bCryptPasswordEncoder)
            .and()
            .build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(null));
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }
}
