////    private UserDetailsService userDetailsService;
////    private final PasswordEncoder passwordEncoder;
//
//    public WebSecurityConfig(SuccessUserHandler successUserHandler) {//,
////                             UserDetailsService userDetailService, PasswordEncoder passwordEncoder) {
//        this.successUserHandler = successUserHandler;
////        this.userDetailsService = userDetailService;
////        this.passwordEncoder = passwordEncoder;
//    }
//
//    //Со Spring Security 5.7 использование configure - deprecated. Теперь используют SecurityFilterChainpublic SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
//    @Override
//    protected void configure(HttpSecurity http) throws Exception { //HttpSecurity - флюент-объект для описания цепочки фильтров безопасности.
//        http
//                .csrf().disable() // по умолчанию, csrf токен хранится в HttpSession, а не в cookie, указывать не обязательно
////                .and()
//                .authorizeRequests()
//                .antMatchers("/", "/index", "/css/*", "/js/*").permitAll() //эти пути открыты для всех
//                .antMatchers("/admin/**").hasAnyRole("ADMIN", "USER")
//                .antMatchers("/user/**").hasRole("USER") //.hasRole("USER") автоматически ищет "ROLE_USER"
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                    .loginPage("/login").successHandler(successUserHandler).permitAll()//.defaultSuccessUrl("/user", true).permitAll()
////                .formLogin()
////                .loginPage("/login")
////                .defaultSuccessUrl("/") // временно
////                .permitAll()
//                .and()
//                .rememberMe()
//                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)) //выставляем длительность сессии
//                    .key("alex!Security@Token#Encryption") //ключ для шифрования remember-me токена, который Spring хранит в cookie
//                .and()
//                .logout()
//                    .logoutUrl("/logout")
//                    .clearAuthentication(true)
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID", "remember-me")
//                    .logoutSuccessUrl("/")
//                .permitAll();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(daoAuthenticationProvider());
//    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder);
//        provider.setUserDetailsService(userDetailsService);
//        return provider;
//    }
//
//    @Bean
//    public UserDetailsService users() {
//        System.out.println("⚠️ InMemoryUserDetailsManager is used!");
////        UserDetails user =
////                User.withDefaultPasswordEncoder()
////                        .username("user")
////                        .password("user")
////                        .roles("USER")
////                        .build();
//
//
//        UserDetails admin = User.withUsername("admin")
//
//                .password("{noop}admin") // убрать все сложности
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(admin);
//    }
////
////
////    @Bean
////    public UserDetailsService users() {
////        UserDetails admin = User.builder()
////                .username("admin")
////                .password("{noon}admin") //admin
////                .roles("ADMIN", "USER")
////                .build();
////
////        return new InMemoryUserDetailsManager(admin);
////    }
//
//    // аутентификация inMemory
////    @Bean
//    //@Override
////    public UserDetailsService users() {// userDetailsService() { //сервис для получения пользователя из БД
////        UserDetails alexUser = User.builder()
////                .username("alex")
////                .password("123")
//////                .password(passwordEncoder.encode("password"))
////                .roles("USER") //ROLE_USER
////                .build();
////
////        UserDetails adminUser = User.builder()
////                .username("admin")
////                .password("{bcrypt}$2a$12$9PJXDPSYjhOlT81N5EHU1eKLUrYFIn6dBlZRUyVSz3jyFbtjiETdq")(passwordEncoder.encode("root"))
////                .roles("ADMIN") //ROLE_ADMIN
////                .build();
////
////        return new InMemoryUserDetailsManager(alexUser, adminUser);
//
//
//
////        UserDetails user =                          //информация о юзере
////                User.withDefaultPasswordEncoder()
////                        .username("user")
////                        .password("user1") //userman
////                        .roles("USER")
////                        .build();
////
////        UserDetails admin = //new
////                User.withDefaultPasswordEncoder()
////                        .username("admin")
////                        .password("root") //root
////                        .roles("USER","ADMIN")
////                        .build();
////
////        return new InMemoryUserDetailsManager(user, admin);
////    }
//
//}