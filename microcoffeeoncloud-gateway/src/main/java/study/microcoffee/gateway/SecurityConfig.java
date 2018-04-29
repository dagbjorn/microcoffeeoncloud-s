package study.microcoffee.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import study.microcoffee.gateway.security.Http401LoginUrlAuthenticationEntryPoint;

/**
 * Configuration class of Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configures global security.
     * <p>
     * The in-memory authentication takes precedence over the <code>spring.security.user.*</code> attributes defined in
     * <code>application.properties</code>.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder //
            .inMemoryAuthentication() //
            .withUser("user").password("{noop}12345678").roles("USER");
    }

    /**
     * Configures the web security of the microservice.
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity //
            .authorizeRequests() //
            .antMatchers("/coffee.html").permitAll() //
            .antMatchers("/css/**", "/js/**").permitAll() //
            .antMatchers("/coffeeshop/nearest/**").permitAll() //
            .antMatchers("/coffeeshop/menu").permitAll() //
            .antMatchers("/coffeeshop/**/order/**").hasRole("USER") //
            .anyRequest().authenticated() //
            .and() //
            .formLogin().defaultSuccessUrl("/coffee.html") //
            .and() //
            .httpBasic() //
            .and() //
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(new Http401LoginUrlAuthenticationEntryPoint("/login"),
                new AntPathRequestMatcher("/coffeeshop/**/order/**"))
            .and() //
            .csrf().disable();
    }
}
