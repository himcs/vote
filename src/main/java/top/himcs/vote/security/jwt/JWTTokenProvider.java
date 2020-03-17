package top.himcs.vote.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTTokenProvider implements TokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    private Key key;

    public JWTTokenProvider(long tokenValidityInMilliseconds, long tokenValidityInMillisecondsForRememberMe, Key key) {
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
        this.tokenValidityInMillisecondsForRememberMe = tokenValidityInMillisecondsForRememberMe;
        this.key = key;
    }

    @Override
    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        long now = new Date().getTime();
        Date validity;
        validity = new Date(now + this.tokenValidityInMilliseconds);
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        return Jwts.builder()

                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .signWith(key)          // (3)

                .compact();
    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = (Claims) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parse(token).getBody();


        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());


        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);

    }

    @Override
    public boolean validateToken(String authToken) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build().parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("Invalid JWT signature.");
            logger.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            logger.info("Expired JWT token.");
            logger.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.info("Unsupported JWT token.");
            logger.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            logger.info("JWT token compact of handler are invalid.");
            logger.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
