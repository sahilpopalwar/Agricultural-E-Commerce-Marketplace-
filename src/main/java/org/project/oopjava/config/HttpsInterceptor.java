package org.project.oopjava.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HttpsInterceptor implements HandlerInterceptor {

    @Value("${server.ssl.enabled:false}")
    private boolean sslEnabled;

    @Value("${app.public-base-url:https://localhost:8443}")
    private String publicBaseUrl;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        if (sslEnabled && !request.isSecure()) {
            String requestURI = request.getRequestURI();
            String queryString = request.getQueryString();
            
            StringBuilder redirectUrl = new StringBuilder(publicBaseUrl)
                      .append(requestURI);
            
            if (queryString != null) {
                redirectUrl.append("?").append(queryString);
            }
            
            response.sendRedirect(redirectUrl.toString());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        // No implementation needed
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                @Nullable Exception ex) throws Exception {
        // No implementation needed
    }
} 