package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/index", "/css/*", "/js/*").permitAll() //эти пути открыты для всех
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN") //.hasRole("USER") автоматически ищет "ROLE_USER"
                .anyRequest().authenticated()
                .and()
                .formLogin()
//                    .loginPage("/login")
                .successHandler(successUserHandler).permitAll()
                .and()
//                .rememberMe()
//                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)) //выставляем длительность сессии
//                    .key("alex!Security@Token#Encryption") //ключ для шифрования remember-me токена, который Spring хранит в cookie
//                .and()
                .logout()
//                    .logoutUrl("/logout")
//                    .clearAuthentication(true)
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID", "remember-me")
//                    .logoutSuccessUrl("/")
                .permitAll();
    }

    // аутентификация inMemory
    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        System.out.println("⚠️ InMemoryUserDetailsManager is used!");

        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("user")
                        .roles("USER")
                        .build();

        UserDetails admin =
                User.builder()
                        .username("admin")
                        .password("{noop}admin")
                        .roles("ADMIN", "USER")
                        .build();

        UserDetails alex =
                User.builder()
                        .username("alex")
                        .password("{noop}alex")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user, admin, alex);
    }
}