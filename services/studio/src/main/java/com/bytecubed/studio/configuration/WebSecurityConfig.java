package com.bytecubed.studio.configuration;

//@Configuration
//@EnableWebSecurity
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/home").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
//                .and()
//                .logout()
//                .permitAll();
//    }
//
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }

//    @Value(value = "${com.auth0.domain}")
//    private String domain;
//    @Value(value = "${com.auth0.clientId}")
//    private String clientId;
//    @Value(value = "${com.auth0.clientSecret}")
//    private String clientSecret;
//
//    @Bean
//    public AuthenticationController authenticationController() throws UnsupportedEncodingException {
//        return AuthenticationController.newBuilder(domain, clientId, clientSecret)
//                .build();
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http
//                .authorizeRequests()
//                .antMatchers("/callback", "/login").permitAll()
//                .antMatchers("/**").authenticated()
//                .and()
//                .logout().permitAll();
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
//    }
}
