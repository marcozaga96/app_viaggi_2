package marcozagaria.app_viaggi_2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Dipendente {
    @Id
    @GeneratedValue()
    @Setter(AccessLevel.NONE)
    private UUID id;
    private String nome;
    private String cognome;
    private String email;
    private String username;
    private String avatar;

    public Dipendente(String nome, String cognome, String email, String username) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.username = username;
    }
}
