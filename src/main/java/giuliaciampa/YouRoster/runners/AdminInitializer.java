package giuliaciampa.YouRoster.runners;

import giuliaciampa.YouRoster.services.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class AdminInitializer implements CommandLineRunner {

    public final AuthService authService;

    public AdminInitializer(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void run(String... args) throws Exception {
        authService.saveAdmin("admin@youroster.com", "Admin1234");
        System.out.println("inizializzazione admin completata");
    }
}
