package top.himcs.vote.validate.smscode;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SmsAuthenticationFilter extends
        AbstractAuthenticationProcessingFilter {

    public static final String TEL_KEY = "tel";
    public static final String CODE_KEY = "code";
    private String telParameter = TEL_KEY;
    private String codeParameter = CODE_KEY;
    private boolean postOnly = true;

    public SmsAuthenticationFilter() {
        super(new AntPathRequestMatcher("/login/mobile", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String tel = obtainTel(request);
        String code = obtainCode(request);

        if (tel == null) {
            tel = "";
        }

        if (code == null) {
            code = "";
        }

        tel = tel.trim();
        code = code.trim();



        SmsAuthenticationToken authToken = new SmsAuthenticationToken(tel, code);
        setDetails(request, authToken);

        return this.getAuthenticationManager().authenticate(authToken);
    }

    protected void setDetails(HttpServletRequest request,
                              SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    private String obtainTel(HttpServletRequest request) {
        return request.getParameter(telParameter);
    }

    private String obtainCode(HttpServletRequest request) {
        return request.getParameter(codeParameter);
    }
}
