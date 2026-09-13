package com.omarproject.storeapi.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class DemoSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery(
                "select name, password, true from Staff where name = ?"
        );

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select s.name, r.name " +
                        "from Staff s " +
                        "join Roles r on s.role_id = r.id " +
                        "where s.name = ?"
        );

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/product/products").hasAnyRole("CASHIER", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/product/products/{productId}").hasAnyRole("CASHIER", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/sale/sales").hasAnyRole("CASHIER", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/sale/sales/{saleId}").hasAnyRole("CASHIER", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/product/products").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/sale/sales").hasAnyRole("CASHIER","MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/product/products").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/sale/sales").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/product/products/{productId}").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PATCH, "/api/sale/sales/{saleId}").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/product/products/{productId}").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/sale/sales/{saleId}").hasRole("MANAGER")
        );

        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}
