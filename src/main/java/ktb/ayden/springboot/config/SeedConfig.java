//임시로 전체주석처리 <- 비밀번호 암호화 확인 및 테스트 용이성 위해

//package ktb.ayden.springboot.config;
//
//import ktb.ayden.springboot.entity.User;
//import jakarta.transaction.Transactional;
//import ktb.ayden.springboot.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//
//import java.util.stream.IntStream;
//
////스프링 설정 클래스임을 나타냄
//@Configuration
////개발 환경에서만 사용하려고 지정해주는 것
//@Profile("development")
//@RequiredArgsConstructor
//public class SeedConfig {
//    private final UserRepository userRepository;
//
//    @Bean
//    ApplicationRunner seedRunner(){
//        return arguments -> seed();
//    }
//    @Transactional
//    void seed(){
//        if(userRepository.count() >= 10) return;
//
//        // tester1 ~ tester10 계정 더미 데이터
////        IntStream.rangeClosed(1, 10).forEach(i -> {
////            String password = "12341234aS!" + i;
////            User user = new User("tester" + i + "@adapterz.kr", password, "tester" + i,"profileImage"+i);
////            userRepository.save(user);
////        });
//        //IntStrem~ 알아보니 일반 반복문 -> 익숙한 for문으로 수정(직관적)
//        for(int i=1;i<=10;i++){
//            String password = "12341234aS!" + i;
//            User user = new User("tester" + i + "@adapterz.kr", password, "tester" + i,"profileImage"+i);
//            userRepository.save(user);
//        }
//    }
//}
