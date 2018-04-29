package study.microcoffee.order.common.hystrix;

import org.springframework.web.context.request.RequestAttributes;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

/**
 * HystrixRequestVariable class used for propagating the RequestAttributes object associated with the request to child threads.
 */
public class HystrixRequestAttributesVariable {

    private static final HystrixRequestVariableDefault<RequestAttributes> requestAttributesVariable = new HystrixRequestVariableDefault<>();

    private HystrixRequestAttributesVariable() {
    }

    public static HystrixRequestVariableDefault<RequestAttributes> getInstance() {
        return requestAttributesVariable;
    }
}
