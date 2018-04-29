package study.microcoffee.gateway.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import study.microcoffee.gateway.SecurityConfig;

/**
 * AuthenticationEntryPoint used to return 401 and a location header with redirect URL if access is defined.
 *
 * @see SecurityConfig
 */
public class Http401LoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(Http401LoginUrlAuthenticationEntryPoint.class);

    public Http401LoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException {

        logger.debug("Pre-authenticated entry point called. Returning 401 Unauthorized insteadof 302.");

        String redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);
        response.addHeader(HttpHeaders.LOCATION, redirectUrl);

        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name());
    }
}
