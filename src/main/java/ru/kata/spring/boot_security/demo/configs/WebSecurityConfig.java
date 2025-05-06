package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.concurrent.TimeUnit;

@Configuration //убрать потом
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    private final SuccessUserHandler successUserHandler;
//    private UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;

//    @Autowired
//    public WebSecurityConfig(SuccessUserHandler successUserHandler,
//                             UserDetailsService userDetailService, PasswordEncoder passwordEncoder) {
//        this.successUserHandler = successUserHandler;
////        this.userDetailsService = userDetailService;
//        this.passwordEncoder = passwordEncoder;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                  .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .sessionManagement().sessionCreationPolicy(sessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll()
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")//.successHandler(successUserHandler)
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                    .key("securitything")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/")
                .permitAll();

//

//                .formLogin().successHandler(successUserHandler)
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
    }

    


    //@Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }

    //@Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder);
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }

    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{bcrypt}$2a$12$9PJXDPSYjhOlT81N5EHU1eKLUrYFIn6dBlZRUyVSz3jyFbtjiETdq") //admin
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    // аутентификация inMemory
//    @Bean
    //@Override
//    public UserDetailsService users() {// userDetailsService() { //сервис для получения пользователя из БД
//        UserDetails alexUser = User.builder()
//                .username("alex")
//                .password("123")
////                .password(passwordEncoder.encode("password"))
//                .roles("USER") //ROLE_USER
//                .build();
//
//        UserDetails adminUser = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$12$9PJXDPSYjhOlT81N5EHU1eKLUrYFIn6dBlZRUyVSz3jyFbtjiETdq")(passwordEncoder.encode("root"))
//                .roles("ADMIN") //ROLE_ADMIN
//                .build();
//
//        return new InMemoryUserDetailsManager(alexUser, adminUser);



//        UserDetails user =                          //информация о юзере
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("user1") //userman
//                        .roles("USER")
//                        .build();
//
//        UserDetails admin = //new
//                User.withDefaultPasswordEncoder()
//                        .username("admin")
//                        .password("root") //root
//                        .roles("USER","ADMIN")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

}