package seba.java.cursoJava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CursoJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursoJavaApplication.class, args);
		System.out.print("Funcionado");
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringApplicationConetxt springApplicationConetxt(){
		return new SpringApplicationConetxt();
	}

}
