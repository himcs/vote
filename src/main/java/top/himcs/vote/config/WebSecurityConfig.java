package top.himcs.vote.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import top.himcs.vote.security.MyUserDetailsService;
import top.himcs.vote.security.jwt.JWTConfigurer;
import top.himcs.vote.security.jwt.JWTTokenProvider;
import top.himcs.vote.security.jwt.TokenProvider;

import java.security.Key;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    //配置忽略路径
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")

                // allow anonymous resource requests
                .antMatchers(
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/h2-console/**"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                // enable h2-console
                .headers()
                .frameOptions()
                .sameOrigin()

                // create no session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                // .antMatchers("/api/register").permitAll()
                // .antMatchers("/api/activate").permitAll()
                // .antMatchers("/api/account/reset-password/init").permitAll()
                // .antMatchers("/api/account/reset-password/finish").permitAll()

                .antMatchers("/api/person").hasAuthority("ROLE_USER")
                .antMatchers("/api/hiddenmessage").hasAuthority("ROLE_ADMIN")

                .anyRequest().authenticated()

                .and()
                .apply(securityConfigurerAdapter());

        // 禁用缓存
        http.headers().cacheControl();

    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*"); // e.g. http://domain1.com
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }


    @Bean
    public JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(jwtTokenProvider());
    }


    @Bean
    public TokenProvider jwtTokenProvider() {
        long tokenValidityInMilliseconds = 3600L;
        long tokenValidityInMillisecondsForRememberMe = 3600L;
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return new JWTTokenProvider(tokenValidityInMilliseconds, tokenValidityInMillisecondsForRememberMe, key);
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }
}
