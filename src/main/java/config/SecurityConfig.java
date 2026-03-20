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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Con esto comenzamos a cofigurar el CORS con el Bean de abajito
            .csrf(AbstractHttpConfigurer::disable) // Lo deshabilitamos pora utilizar en su lugar JWT
            .authorizeHttpRequests(auth -> auth // Aquí es donde manejamos la autenticación con JWT en los endpoints
                    .anyRequest().permitAll() // Cuando implementemos JWT, cambiaremos esto a .authenticated() y definiremos los roles de los usuarios
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // URL del front (Por defecto puerto 5173 para REact)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // Métodos HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cabeceras (Authorization es para JWT)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Credenciales para el modulo de webSockets
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica CORS a todas los endpoints del backend
        return source;
    }
}
