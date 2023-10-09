package com.oxygensend.backend.application.auth;

import com.oxygensend.backend.domain.auth.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    @Override
    public User getAuthenticationPrinciple() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
