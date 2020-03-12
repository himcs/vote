package top.himcs.vote.seicurity.broswer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import top.himcs.vote.seicurity.broswer.handler.BrowserAuthenticationFailureHandler;
import top.himcs.vote.seicurity.broswer.handler.BrowserAuthenticationSuccessHandler;
import top.himcs.vote.validate.code.ValidateCodeFilter;
import top.himcs.vote.validate.smscode.SmsAuthenticationFilter;
import top.himcs.vote.validate.smscode.SmsAuthenticationProvider;

//@EnableWebSecurity
public class BrowserSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    ValidateCodeFilter validateCodeFilter;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/css/*"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //使用Session的方式保存
        http.formLogin()
                .loginPage("/authentication/require") // 登录跳转 URL
                .loginProcessingUrl("/login")
                .failureHandler(authenticationFailureHandler())
                .and()

                .authorizeRequests() // 授权配置
                .antMatchers("/code/sms",
                        "/authentication/require",
                        "/code/image",
                        "/current"
                ).permitAll()
                .anyRequest()  // 所有请求
                .authenticated()   // 都需要认证
                .and().csrf().disable();
        http.addFilterBefore(smsAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public SmsAuthenticationFilter smsAuthenticationFilter() throws Exception {
        SmsAuthenticationFilter filter = new SmsAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManagerBean());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return filter;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new BrowserAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new BrowserAuthenticationSuccessHandler();
    }

    @Bean
    public SmsAuthenticationProvider smsAuthenticationProvider() {
        return new SmsAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
