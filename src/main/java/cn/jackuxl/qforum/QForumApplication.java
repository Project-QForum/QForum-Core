package cn.jackuxl.qforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@MapperScan("cn.jackuxl.qforum")
public class QForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(QForumApplication.class, args);
    }

}
