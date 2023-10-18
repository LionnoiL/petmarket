package org.petmarket.security.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.petmarket.errorhandling.ItemNotFoundException;
import org.petmarket.security.jwt.JwtResponseDto;
import org.petmarket.security.jwt.JwtTokenProvider;
import org.petmarket.users.entity.LoginProvider;
import org.petmarket.users.entity.Role;
import org.petmarket.users.entity.User;
import org.petmarket.users.entity.UserStatus;
import org.petmarket.users.repository.RoleRepository;
import org.petmarket.users.repository.UserRepository;
import org.petmarket.users.service.UserService;
import org.petmarket.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${site.login.oauth.url}")
    private String frontendLoginOAuthUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();

        String email = attributes.getOrDefault("email", "").toString();
        Optional<User> byEmail = userRepository.findByEmail(email);
        User user = null;

        if (!byEmail.isPresent()) {
            user = new User();
            Role roleUser = roleRepository.findByName("ROLE_USER").orElseThrow(() -> {
                throw new ItemNotFoundException("Role not found");
            });
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(roleUser);
            user.setEmail(email);

            user.setPassword(passwordEncoder.encode(Helper.getRandomString(10)));
            user.setRoles(userRoles);
            user.setStatus(UserStatus.ACTIVE);
            user.setId(null);

            if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                user.setFirstName(attributes.getOrDefault("given_name", "").toString());
                user.setLastName(attributes.getOrDefault("family_name", "").toString());
                user.setLoginProvider(LoginProvider.GOOGLE);
            } else if ("facebook".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                String name = attributes.getOrDefault("name", "").toString();
                String[] names = name.split(" ");
                user.setFirstName(names[0]);
                user.setLastName(names[1]);
                user.setLoginProvider(LoginProvider.FACEBOOK);
            }

            userRepository.save(user);

        } else {
            user = byEmail.get();
        }

        String token = jwtTokenProvider.createToken(email, user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(email, user.getRoles());
        JwtResponseDto jwtResponseDto = new JwtResponseDto(email, token, refreshToken);

        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(jwtResponseDto));

        response.sendRedirect(frontendLoginOAuthUrl);
    }
}
