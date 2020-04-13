package com.hangyasi.study.nameserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * A szerver elsődleges belépési pontja, itt indul a spring application context-je, ami létrehozza
 * a spring által kezelt beaneket, regisztrálja az implementált REST végpontokat.
 *
 * @author Hangyási Zoltán
 * @since 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class NameServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NameServerApplication.class, args);
	}
}
