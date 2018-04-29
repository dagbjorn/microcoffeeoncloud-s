package study.microcoffee.order.common.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException.FailureType;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;

/**
 * HystrixCommandExecutionHook implementation used to set the RequestAttributes object associated with the request in the
 * RequestContextHolder of the current thread. Upon exit, successful or not, the RequestContextHolder is reset.
 */
public class HystrixRequestAttributesExecutionHook extends HystrixCommandExecutionHook {

    private Logger logger = LoggerFactory.getLogger(HystrixRequestAttributesExecutionHook.class);

    @Override
    public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {
        RequestContextHolder.setRequestAttributes(HystrixRequestAttributesVariable.getInstance().get());

        logger.debug("RequestContextHolder is now set; requestAttributes={}", RequestContextHolder.getRequestAttributes());
    }

    @Override
    public <T> void onSuccess(HystrixInvokable<T> commandInstance) {
        resetRequestAttributes();
    }

    @Override
    public <T> T onEmit(HystrixInvokable<T> commandInstance, T value) {
        resetRequestAttributes();

        return value;
    }

    @Override
    public <T> Exception onError(HystrixInvokable<T> commandInstance, FailureType failureType, Exception e) {
        resetRequestAttributes();

        return e;
    }

    private void resetRequestAttributes() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            RequestContextHolder.resetRequestAttributes();

            logger.debug("RequestContextHolder is now cleared");
        }
    }
}
