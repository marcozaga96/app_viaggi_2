package marcozagaria.app_viaggi_2.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import marcozagaria.app_viaggi_2.entities.Dipendente;
import marcozagaria.app_viaggi_2.exeption.UnauthorizedException;
import marcozagaria.app_viaggi_2.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    JWT jwt;
    @Autowired
    DipendenteService dipendenteService;

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


        // ******************************************************* AUTORIZZAZIONE ****************************************************************
        // Se voglio abilitare le regole di AUTORIZZAZIONE, devo "informare" Spring Security su chi sia l'utente che sta effettuando questa richiesta
        // così facendo Spring Security riuscirà a controllarne il ruolo per poi nei vari endpoint poter utilizzare l'annotazione @PreAuthorize
        // specifica per il controllo ruoli
        // 1. Cerco l'utente tramite id (l'id l'abbiamo messo nel token!)
        String userId = jwt.getToken(accessToken);
        Dipendente dipendenteLoggato = dipendenteService.cercaId(UUID.fromString(userId));
        // 2. Trovato l'utente posso associarlo al cosiddetto Security Context, questa è la maniera per Spring Security di associare l'utente alla
        // richiesta corrente
        Authentication authentication = new UsernamePasswordAuthenticationToken(dipendenteLoggato, null, dipendenteLoggato.getAuthorities());
        // Il terzo parametro serve per poter utilizzare i vari @PreAuthorize perchè così il SecurityContext saprà quali sono i ruoli dell'utente
        // che sta effettuando la richiesta
        SecurityContextHolder.getContext().setAuthentication(authentication); // Aggiorniamo il SecurityContext associandogli l'utente autenticato

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
