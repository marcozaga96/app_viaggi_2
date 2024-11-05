package marcozagaria.app_viaggi_2.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import marcozagaria.app_viaggi_2.enums.Ruolo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties({"password", "email", "ruolo", "authorities", "accountNonLocked", "credentialsNonExpired", "accountNonExpired", "enabled"})
public class Dipendente implements UserDetails {
    @Id
    @GeneratedValue()
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String nome;
    private String cognome;
    private String email;
    private String username;
    private String password;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    public Dipendente(String nome, String cognome, String email, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
        this.ruolo = Ruolo.USER;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Questo metodo deve tornare una lista di ruoli dell'utente. Più in dettaglio vuole che venga restituita una lista di oggetti che implementano
        // GrantedAuthority. SimpleGrantedAuthority è una classe che rappresenta i ruoli degli utenti nel mondo Spring Security
        // ed implementa GrantedAuthority, quindi dobbiamo prendere il nostro ruolo (enum) e passare il name()
        // di quel ruolo al costruttore dell'oggetto
        return List.of(new SimpleGrantedAuthority(this.ruolo.name()));
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }
}
