package dev.ime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition (info =
@Info(
          title = "ms-orders",
          version = "1.0",
          description = "API ms-orders",
          license = @License(name = "GNU GPLv3", url = "https://choosealicense.com/licenses/gpl-3.0/"),
          contact = @Contact(url = "https://github.com/Ivan-Montes", name = "IvanM", email = "ivan@github.com")
  )
)
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MsOrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrdersApplication.class, args);
	}

}
