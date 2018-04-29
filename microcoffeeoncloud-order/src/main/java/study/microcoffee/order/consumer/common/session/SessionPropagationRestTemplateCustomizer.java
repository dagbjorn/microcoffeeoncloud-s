package study.microcoffee.order.consumer.common.session;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate customizer to add a SessionPropagationHttpRequestInterceptor to the RestTemplate instance.
 */
@Component
public class SessionPropagationRestTemplateCustomizer implements RestTemplateCustomizer {

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.getInterceptors().add(new SessionPropagationHttpRequestInterceptor());
    }
}
