package study.microcoffee.gateway.session;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * Zuul filter for forwarding the current session by a JSESSIONID cookie.
 */
@Component
public class SessionForwardingZuulPreFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(SessionForwardingZuulPreFilter.class);

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpSession httpSession = context.getRequest().getSession();

        String encodedSessionId = Base64Utils.encodeToString(httpSession.getId().getBytes());
        context.addZuulRequestHeader("Cookie", "JSESSIONID=" + encodedSessionId);

        logger.info("ZuulPreFilter session proxy: httpSessionId={}, Base64 encoded httpSessionId={}", httpSession.getId(),
            encodedSessionId);

        return null;
    }
}
