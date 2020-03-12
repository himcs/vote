package top.himcs.vote.seicurity.jwt;

import org.springframework.security.core.Authentication;

public interface TokenProvider {
    //生成Token
    public String createToken(Authentication authentication, boolean rememberMe);

    //根据Token生成用户
    public Authentication getAuthentication(String token);

    public boolean validateToken(String authToken);

}
