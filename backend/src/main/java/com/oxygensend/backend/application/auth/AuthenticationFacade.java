package com.oxygensend.backend.application.auth;

import com.oxygensend.backend.domain.auth.User;

public interface AuthenticationFacade {

    User getAuthenticationPrinciple();
}
