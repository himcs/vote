package top.himcs.vote.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.himcs.vote.security.jwt.JWTFilter;

import java.util.Arrays;

public class MyUserDetailsService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    public MyUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    //自定义根据用户名返回相应的用户详情
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("user hhh");
        return new User(username, passwordEncoder.encode(username), Arrays.asList(new SimpleGrantedAuthority("good")));
    }
}
