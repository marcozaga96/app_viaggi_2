package marcozagaria.app_viaggi_2.services;

import marcozagaria.app_viaggi_2.entities.Dipendente;
import marcozagaria.app_viaggi_2.exeption.UnauthorizedException;
import marcozagaria.app_viaggi_2.payloads.LoginDTO;
import marcozagaria.app_viaggi_2.security.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    private DipendenteService dipendenteService;
    @Autowired
    private JWT jwt;

    public String controllaCredenziali(LoginDTO body) {
// 1.1 Cerco nel DB se esiste un utente con l'email fornita
        Dipendente found = dipendenteService.findByEmail(body.email());
        if (found.getPassword().equals(body.password())) {
            // 2. Se sono OK --> Genero il token
            String accessToken = jwt.createToken(found);
            // 3. Ritorno il token
            return accessToken;
        } else {
            // 4. Se le credenziali sono errate --> 401 (Unauthorized)
            throw new UnauthorizedException("Credenziali errate!");
        }
    }
}
