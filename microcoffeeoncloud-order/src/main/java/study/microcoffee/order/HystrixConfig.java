package study.microcoffee.order;

import javax.annotation.PostConstruct;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import com.netflix.hystrix.strategy.HystrixPlugins;

import study.microcoffee.order.common.hystrix.HystrixRequestAttributesExecutionHook;
import study.microcoffee.order.common.hystrix.HystrixRequestAttributesFilter;

/**
 * Configuration class of Hystrix.
 */
@Configuration
@EnableCircuitBreaker
@EnableHystrixDashboard
public class HystrixConfig {

    /**
     * Registers a Hystrix command execution hook used to propagate the RequestContext to child threads calling the CreditRating
     * REST service.
     */
    @PostConstruct
    public void init() {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new HystrixRequestAttributesExecutionHook());
    }

    /**
     * Registers the servlet filter used to initialize a HystrixRequestContext and create a HystrixRequestVariable for storing the
     * RequestAttributes to propagate child threads.
     *
     * @return The filter registration bean.
     */
    @Bean
    public FilterRegistrationBean<HystrixRequestAttributesFilter> hystrixRequestAttributesFilterRegistrationBean() {
        FilterRegistrationBean<HystrixRequestAttributesFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HystrixRequestAttributesFilter());
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }
}
