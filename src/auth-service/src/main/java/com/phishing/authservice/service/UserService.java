package com.phishing.authservice.service;

import com.phishing.authservice.component.passport.Passport;
import com.phishing.authservice.component.token.TokenResolver;
import com.phishing.authservice.domain.User;
import com.phishing.authservice.payload.request.EditProfileRequest;
import com.phishing.authservice.payload.request.SignUpRequest;
import com.phishing.authservice.exception.exceptions.DuplicateEmailException;
import com.phishing.authservice.payload.token.MemberInfo;
import com.phishing.authservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenResolver tokenResolver;

    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email already exists");
        }
    }

    public void signUp(SignUpRequest request) {
        checkEmail(request.email());
        User user = User.signUp(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.nickname(),
                request.phnum()
        );
        userRepository.save(user);
    }

    public void editProfile(Passport passport, EditProfileRequest request){
        User targetUser = userRepository.findByIdAndIsDeletedIsFalse(passport.userId())
                .orElseThrow(() -> new DuplicateEmailException("Email Not exists"));

        targetUser.editProfile(
                request.nickname(),
                request.phnum()
        );
    }

    public void resignUser(Passport passport) {
        Long userId = passport.userId();
        User targetUser = userRepository.findByIdAndIsDeletedIsFalse(userId)
                .orElseThrow(() -> new DuplicateEmailException("Email Not exists"));
        targetUser.resignUser();
    }

    private Long getMemberInfoToToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return tokenResolver.getAccessClaims(token);
    }
}
