package com.university.management.system.utils;

import com.university.management.system.models.users.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Person) {
            return ((Person) authentication.getPrincipal()).getId();
        }
        return null;
    }
}
