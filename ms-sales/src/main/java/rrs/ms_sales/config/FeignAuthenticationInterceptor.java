package rrs.ms_sales.config;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;


@Component
public class FeignAuthenticationInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            String token = requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            if (token != null && !token.isBlank()) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, token);
            }
        }
    }

}
