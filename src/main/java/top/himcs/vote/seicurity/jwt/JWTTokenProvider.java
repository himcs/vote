package top.himcs.vote.seicurity.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JWTTokenProvider implements TokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);

    @Override
    public String createToken(Authentication authentication, boolean rememberMe) {
        return null;
    }

    @Override
    public Authentication getAuthentication(String token) {

        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");

        User principal = new User(token, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);

    }

    @Override
    public boolean validateToken(String authToken) {
        return true;
    }
}
