package marcozagaria.app_viaggi_2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marcozagaria.app_viaggi_2.exeption.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    JWT jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Questo filtro dovrà controllare che il token allegato alla richiesta sia valido. Il token lo troveremo nell'Authorization Header (se c'è)
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer "))
            throw new UnauthorizedException("Inserire token nell'Authorization Header nel formato corretto!");
        // 2. Estraiamo il token dall'header
        String accessToken = header.substring(7);
        // il 7 sta ad indicare il numero di caratteri da saltare nella stringa da prendere
        // 3. Verifichiamo se il token è stato manipolato (verifichiamo la signature) o se è scaduto (verifichiamo Expiration Date)
        jwt.verifyToken(accessToken);
        // 4. Se tutto è OK, andiamo avanti (passiamo la richiesta al prossimo filtro o al controller)
        filterChain.doFilter(request, response); // Tramite .doFilter(req,res) richiamo il prossimo membro della catena (o un filtro o un controller)
        // 5. Se qualcosa non va con il token --> 401
    }

    // Voglio disabilitare il filtro per tutte le richieste al controller Auth, quindi tutte le richieste che avranno come URL /auth/** non dovranno
    // avere il controllo del token
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/autorizzato/**", request.getServletPath());
    }
}
