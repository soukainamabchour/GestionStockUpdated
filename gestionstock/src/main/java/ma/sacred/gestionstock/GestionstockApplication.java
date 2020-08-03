package ma.sacred.gestionstock;

import ma.sacred.gestionstock.Dao.MelangeReferenceRepository;
import ma.sacred.gestionstock.Services.IStockInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionstockApplication implements CommandLineRunner {

	@Autowired
	IStockInitService service;

	@Autowired
	MelangeReferenceRepository melangeReferenceRepository;
	public static void main(String[] args) {
		SpringApplication.run(GestionstockApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		service.initMelangeEmplacement();
		service.initMelangeRef();
		service.initMelange();

	}
}
