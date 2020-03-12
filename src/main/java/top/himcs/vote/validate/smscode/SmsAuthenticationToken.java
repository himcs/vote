package top.himcs.vote.validate.smscode;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private String tel;
    private String code;

    public SmsAuthenticationToken(String tel, String code) {
        super(null);
        this.tel = tel;
        this.code = code;
    }

    @Override
    public Object getCredentials() {
        return getCode();
    }

    @Override
    public Object getPrincipal() {
        return getTel();
    }


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
