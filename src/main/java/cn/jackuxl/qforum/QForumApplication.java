package cn.jackuxl.qforum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.jackuxl.qforum.mapper")
public class QForumApplication {
    public static void main(String[] args) {

        SpringApplication.run(QForumApplication.class, args);
    }

}
