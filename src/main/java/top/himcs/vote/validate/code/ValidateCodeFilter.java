package top.himcs.vote.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.filter.OncePerRequestFilter;
import top.himcs.vote.web.controller.ValidateController;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class ValidateCodeFilter extends OncePerRequestFilter {
    public static final String FORM_VALIDATE_CODE_KEY = "imageCode";

    private String validateCodeParameter = FORM_VALIDATE_CODE_KEY;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("/login".equalsIgnoreCase(request.getRequestURI()) && HttpMethod.POST.toString().equalsIgnoreCase(request.getMethod())) {
            try {
                validateCode(request);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    public void validateCode(HttpServletRequest request) throws ValidateCodeException {

        ImageCode needCode = getNeedValidateCode(request);
        String codeInRequest = obtainCode(request);

        if (needCode == null || codeInRequest == null) {
            throw new ValidateCodeException("validate code must be not null");
        }

        if (!needCode.getCode().equals(codeInRequest)) {
            throw new ValidateCodeException("error validate code");
        }


    }


    private ImageCode getNeedValidateCode(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        ImageCode imageCode = (ImageCode) httpSession.getAttribute(ValidateController.SESSION_KEY_IMAGE_CODE);
        httpSession.removeAttribute(ValidateController.SESSION_KEY_IMAGE_CODE);
        return imageCode;
    }


    private String obtainCode(HttpServletRequest request) {
        return request.getParameter(getValidateCodeParameter());
    }

    private String getValidateCodeParameter() {
        return validateCodeParameter;
    }

    public void setValidateCodeParameter(String validateCodeParameter) {
        this.validateCodeParameter = validateCodeParameter;
    }
}
