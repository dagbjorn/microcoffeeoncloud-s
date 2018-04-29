package study.microcoffee.order.consumer.common.session;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * ClientHttpRequestInterceptor class for propagating the current HTTP session to a downstream service. The session ID is
 * transferred by a JSESSIONID cookie.
 */
public class SessionPropagationHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(SessionPropagationHttpRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        logger.debug("Request attributes: {}", requestAttributes);

        if (requestAttributes != null) {

            String sessionId = requestAttributes.getSessionId();

            if (sessionId != null) {
                String encodedSessionId = Base64Utils.encodeToString(sessionId.getBytes());

                logger.debug("Propagating sessionId={} (encoded={}) to downstream service", sessionId, encodedSessionId);

                request.getHeaders().add("Cookie", "JSESSIONID=" + encodedSessionId);
            } else {
                logger.warn("No session to propagate to downstream service");
            }
        }

        return execution.execute(request, body);
    }
}
