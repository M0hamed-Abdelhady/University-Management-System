package com.university.management.system.services.auth;

import com.university.management.system.utils.GradesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Component("authorizationService")
@RequiredArgsConstructor
public class AuthorizationService {

    public boolean isValidGrade(String grade) {
        if (!GradesUtils.isValidGrade(grade)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid grade: " + grade + ". Valid grades are: " + GradesUtils.GRADES);
        }
        return true;
    }
}

