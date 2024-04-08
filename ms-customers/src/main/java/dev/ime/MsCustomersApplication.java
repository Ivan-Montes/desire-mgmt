package dev.ime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition (info =
@Info(
          title = "ms-customers",
          version = "1.0",
          description = "API ms-customers",
          license = @License(name = "GNU GPLv3", url = "https://choosealicense.com/licenses/gpl-3.0/"),
          contact = @Contact(url = "https://github.com/Ivan-Montes", name = "IvanM", email = "ivan@github.com")
  )
)
@SpringBootApplication
@EnableDiscoveryClient
public class MsCustomersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCustomersApplication.class, args);
	}

}
