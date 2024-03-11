package org.petmarket.security.front;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.petmarket.errorhandling.BadFrontendTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FrontTokenService {

    @Value("${front.token.secret}")
    private String frontToken;

    public void checkToken(FrontTokenRequestDto tokenRequestDto) {
        if (tokenRequestDto == null || tokenRequestDto.getToken() == null) {
            throw new BadFrontendTokenException("Frontend token is empty");
        }
        if (!frontToken.equals(tokenRequestDto.getToken())) {
            throw new BadFrontendTokenException("Bad frontend token");
        }
    }
}
