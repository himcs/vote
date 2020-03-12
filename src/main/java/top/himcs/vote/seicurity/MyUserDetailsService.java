package top.himcs.vote.seicurity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

public class MyUserDetailsService implements UserDetailsService {
    //自定义根据用户名返回相应的用户详情
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new User(username, username, Arrays.asList(new SimpleGrantedAuthority("good")));
    }
}
