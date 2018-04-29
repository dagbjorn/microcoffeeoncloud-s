package study.microcoffee.creditrating;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Configuration class of HTTP request logging filter.
 * <p>
 * Configure the logging filter in <code>application.properties</code> like:
 *
 * <pre>
 * logging.level.org.springframework.web.filter.CommonsRequestLoggingFilter = DEBUG
 * </pre>
 */
@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public CommonsRequestLoggingFilter requestLogFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA : ");

        return filter;
    }

    /**
     * Registers the servlet filter used for request logging.
     *
     * @return The filter registration bean.
     */
    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> requestLogFilterRegistrationBean() {
        FilterRegistrationBean<CommonsRequestLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(requestLogFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
