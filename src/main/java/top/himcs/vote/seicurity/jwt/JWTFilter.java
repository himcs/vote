package top.himcs.vote.seicurity.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter  extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private TokenProvider tokenProvider;
    private String AUTHORIZATION_HEADER = "token";

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();
        if(validateJWT(jwt)) {
            Authentication authentication = contextSaveAuthentication(jwt);
            logger.debug("set jwt for '{}',uri: '{}'", authentication.getName(),requestURI);
        }
      else {
            logger.debug("valid jwt error,uri: '{}'", requestURI);
        }

        filterChain.doFilter(request, response);
    }

    protected Authentication contextSaveAuthentication(String jwt) {
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication); //核心步骤 将用户信息填充到容器中
        return authentication;
    }

    private boolean validateJWT(String jwt) {
        return StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt);
    }

    private String resolveToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }
}
