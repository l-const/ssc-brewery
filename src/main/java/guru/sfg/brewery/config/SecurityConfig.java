package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncodingEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager auth) {
    RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
    filter.setAuthenticationManager(auth);
    return filter;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
            .csrf().disable();
    http.authorizeRequests(
            authorize ->
                authorize
                    .antMatchers("/", "/webjars/**", "/login", "/resources/**")
                    .permitAll()
                    .antMatchers("/beers/find", "beers/*")
                    .permitAll()
                    .antMatchers(HttpMethod.GET, "/api/v1/beer/**")
                    .permitAll()
                    .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}")
                    .permitAll())
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .formLogin()
        .and()
        .httpBasic();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("spring")
        .password("{bcrypt}$2a$12$DF5OetOtvksPyn2LfmNW3eW8bnN24hBcJIlo1ir3KqgxfMBY9ZWLq")
        .roles("ADMIN")
        .and()
        .withUser("user")
        .password(
            "{sha256}3d456440e338420281b23c91e5c4c4ffb918faf16d2e66d6d73419278e61aaa64407bf005c882899")
        .roles("USER");

    auth.inMemoryAuthentication()
        .withUser("scott")
        .password("{bcrypt15}$2a$15$R0Z5nLCPAz5ObpBAkONJ4eDLk8IkM3TE0W0L0fv1UX8SnshhA3rCy")
        .roles("CUSTOMER");
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return SfgPasswordEncodingEncoderFactories.createDelegatingPasswordEncoder();
  }
  ;

  //    @Override
  //    @Bean
  //    protected UserDetailsService userDetailsService() {
  //        UserDetails admin = User.withDefaultPasswordEncoder()
  //                .username("spring")
  //                .password("guru")
  //                .roles("ADMIN")
  //                .build();
  //        UserDetails user = User.withDefaultPasswordEncoder()
  //                .username("user")
  //                .password("password")
  //                .roles("USER")
  //                .build();
  //
  //        return new InMemoryUserDetailsManager(admin, user);
  //    }

}
