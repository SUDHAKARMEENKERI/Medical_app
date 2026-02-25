package ecommerce_app.medicalStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@org.springframework.context.annotation.ComponentScan(basePackages = { "controller", "service" })
@EnableJpaRepositories(basePackages = "dao")
@EntityScan(basePackages = "modal")
public class MedicalStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedicalStoreApplication.class, args);
	}

}
