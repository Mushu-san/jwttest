package com.example.jwttest.components;

import com.example.jwttest.dto.UserDto;
import com.google.gson.Gson;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

/**
 * User: Angelo
 * Date: 14/05/2023
 * Time: 17:54
 */
public class UserLocator implements HandlerMethodArgumentResolver, AuditorAware<String> {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.getParameterType().equals(UserDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer container, NativeWebRequest request,
                                  WebDataBinderFactory factory) throws Exception {

        return this.extractUser();
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(this.extractUser().getUsername());
    }


    private UserDto extractUser() {
        return Optional
                .ofNullable(RequestContextHolder.getRequestAttributes())
                .map(r -> (UserDto) r.getAttribute("user_attribute", RequestAttributes.SCOPE_REQUEST))
                .orElseGet(UserDto::new);
    }
}
