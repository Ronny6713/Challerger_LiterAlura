package com.alura.Challenger_Literalura;

import com.alura.Challenger_Literalura.main.Main;
import com.alura.Challenger_Literalura.repository.AutoresRepository;
import com.alura.Challenger_Literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengerLiteraluraApplication  implements CommandLineRunner {
	@Autowired
	private AutoresRepository autoresRepository;
	private LibroRepository libroRepository;
	@Autowired
	private Main menu;
	public static void main(String[] args) {
		SpringApplication.run(ChallengerLiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		menu.mostrarMenu();


	}
}
