package ktb.ayden.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//bcrypt는 해시 알고리즘 관련
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    //스프링 빈에 등록
    @Bean
    //PasswordEncdoer -> BCryptPasswordEncoder객체 주입
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}