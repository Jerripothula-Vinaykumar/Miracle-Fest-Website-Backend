package com.fest.backend.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private  final UserDetailsService userDetailsService;
    private final  JwtAuthenticatoinFilter jwtAuthenticatoinFilter;

    public WebSecurityConfig(UserDetailsService userDetailsService, JwtAuthenticatoinFilter jwtAuthenticatoinFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticatoinFilter = jwtAuthenticatoinFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain ( HttpSecurity httpSecurity ) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/api/auth/login", "/api/auth/signup" , "/api/token/refresh").permitAll()
                                .requestMatchers("/api/auth/hello").authenticated()
                                .anyRequest()
                                .authenticated()
                )

                .addFilterBefore(jwtAuthenticatoinFilter , UsernamePasswordAuthenticationFilter.class);

               // .httpBasic(Customizer.withDefaults());


        return httpSecurity.build();
    }

    //@Bean
    public UserDetailsService userDetailsService ()
    {
        UserDetails vinay
                = User.withUsername("Vinay")
                .password("{noop}33333")
                .roles("USER")
                .build();
        UserDetails kumar
                = User.withUsername("kumar")
                .password("{noop }333222")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(vinay , kumar);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder ()
    {
        return new BCryptPasswordEncoder(9);
    }

    @Bean
    public AuthenticationProvider authenticationProvider ()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        //provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager ( AuthenticationConfiguration authenticationConfiguration ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
