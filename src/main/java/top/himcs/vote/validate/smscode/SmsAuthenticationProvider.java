package top.himcs.vote.validate.smscode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import top.himcs.vote.repository.SmsCodeRepository;

public class SmsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    protected final Log logger = LogFactory.getLog(getClass());

    private UserDetailsService userDetailsService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken token = (SmsAuthenticationToken) authentication;
        try {
            if (!checkTelCodeMatch(token)) //未通过验证
            {
                throw new BadCredentialsException("手机号或验证码不正确");
            }

            //根据手机号码，获取登录账号
            //根据登录账号，查找登录人信息....
            UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        } catch (BadCredentialsException e) {
            throw e;
        }
    }

    private boolean checkTelCodeMatch(SmsAuthenticationToken smsAuthenticationToken) {
        System.out.println((String) smsAuthenticationToken.getPrincipal());
        SmsCode needCode = SmsCodeRepository.smsCodeList.get((String) smsAuthenticationToken.getPrincipal());
        if (needCode == null)
            return false;
        return ((String) (smsAuthenticationToken.getCredentials())).equals(needCode.getCode());
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (SmsAuthenticationToken.class
                .isAssignableFrom(authentication));
    }
}
