package dev.andreewcodes.ecotrack.api;

import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Punto de entrada de la API REST de EcoTrack. */
@SpringBootApplication(scanBasePackages = "dev.andreewcodes.ecotrack")
public class EcotrackApiApplication {

    /** Arranca el servidor embebido (por defecto en el puerto 8080). */
    public static void main(String[] args) {
        SpringApplication.run(EcotrackApiApplication.class, args);
    }

    /** Reloj inyectable para que las pruebas fijen la fecha de recolección. */
    @Bean
    Clock reloj() {
        return Clock.systemDefaultZone();
    }

    /** Permite que el frontend de Vite (otro puerto) llame a /api. */
    @Bean
    WebMvcConfigurer cors(@Value("${ecotrack.cors.origenes}") String[] origenes) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(origenes)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .exposedHeaders("Content-Disposition");
            }
        };
    }
}
