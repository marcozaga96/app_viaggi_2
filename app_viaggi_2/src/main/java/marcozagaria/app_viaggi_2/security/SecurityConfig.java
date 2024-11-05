package marcozagaria.app_viaggi_2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//configurazione base per la classe di configurazione security
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
// Se voglio poter utilizzare le regole di AUTORIZZAZIONE con @PreAuthorize è OBBLIGATORIA questa annotazione
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Non voglio il form di login (avremo React per quello)
        httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable());
        // Non voglio la protezione da CSRF
        httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        // Non vogliamo utilizzare le Sessioni
        httpSecurity.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // Disabilitiamo il 401 che riceviamo di default
        httpSecurity.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry.requestMatchers("/**").permitAll());

        /* parte inerente al collegamento tra il db e la pagina web
        //httpSecurity.cors(Customizer.withDefaults()); // <-- OBBLIGATORIA SE VOGLIAMO CONFIGURARE I CORS GLOBALMENTE CON UN BEAN
        
         */
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(12);
    }

    /*  parte inerente al collegamento tra il db e la pagina web
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://www.mywonderfulfrontend.com"));
        // Mi creo una whitelist di uno o più indirizzi FRONTEND che voglio che possano accedere a questo backend.
        // Volendo (anche se rischioso) potrei permettere l'accesso a tutti mettendo "*"
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applica ad ogni URL del mio backend la configurazione di sopra
        return source;
    } // Non dimentichiamoci di aggiungere httpSecurity.cors(Customizer.withDefaults()); alla filter chain!!!!

     */
}
