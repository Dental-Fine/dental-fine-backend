package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuracion de CORS utilizando el BEAN para manipular los endpoints
            .csrf(AbstractHttpConfigurer::disable) // Lo desactivamos por utilizaremos JWT en su lugar
            .authorizeHttpRequests(auth -> auth // Permitimos que todos los endpoints entren sin autenticarse.
                    .anyRequest().permitAll() // Cuando ya vayamos a configurar los JWT, cambiamos esto a: .authenticated() y metemos los roles de usuario.
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // URL del front (Por defecto 5173)
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        // Metodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS"));

        // Cabeceras ("Authorization" es para JWTz)
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Credenciales para el modulo de WebSocket
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Con esto configuramos el cors en todos los Endpoints
        return source;
    }
}
