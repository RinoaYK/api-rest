package dio.api_rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@OpenAPIDefinition(servers = {@Server(url = "postgresql://postgres:DURpVZDPoKNzUbggZjijyPQaEaeRTJBn@nozomi.proxy.rlwy.net:50825/railway", description = "Railway Server")})
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
				.profiles("prd")
				.run(args);
	}
}
