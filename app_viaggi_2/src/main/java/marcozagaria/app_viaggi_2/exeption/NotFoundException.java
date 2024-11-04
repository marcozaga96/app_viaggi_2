package marcozagaria.app_viaggi_2.exeption;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("l'id: " + id + " no è stato trovato");
    }
}
