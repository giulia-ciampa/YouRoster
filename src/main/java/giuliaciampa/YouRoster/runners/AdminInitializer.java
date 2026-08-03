package giuliaciampa.YouRoster.runners;

import giuliaciampa.YouRoster.services.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class AdminInitializer implements CommandLineRunner {

    public final AuthService authService;
    private final String password;

    public AdminInitializer(AuthService authService, @Value("${password.admin}") String password) {
        this.authService = authService;
        this.password = password;
    }

    @Override
    public void run(String... args) throws Exception {
        authService.saveAdmin("admin@youroster.com", password);
        System.out.println("inizializzazione admin completata");
    }
}
