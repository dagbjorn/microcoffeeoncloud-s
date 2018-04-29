package study.microcoffee.order.common.hystrix;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * Servlet filter that initialises a HystrixRequestContext and creates a HystrixRequestVariable for storing the RequestAttributes to
 * propagate child threads.
 */
public class HystrixRequestAttributesFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        HystrixRequestAttributesVariable.getInstance().set(RequestContextHolder.getRequestAttributes());

        try {
            filterChain.doFilter(request, response);
        } finally {
            context.shutdown();
        }
    }
}
