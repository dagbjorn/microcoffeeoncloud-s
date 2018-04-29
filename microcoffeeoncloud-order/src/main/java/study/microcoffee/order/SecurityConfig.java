package study.microcoffee.order;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configuration class of Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configures the web security of the microservice.
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity //
            .httpBasic() // Needed for testing with curl
            .and() //
            .authorizeRequests() //
            .antMatchers("/coffeeshop/menu").permitAll() //
            .antMatchers("/coffeeshop/**/order").hasRole("USER") //
            .anyRequest().authenticated() //
            .and() //
            .csrf().disable();
    }
}
