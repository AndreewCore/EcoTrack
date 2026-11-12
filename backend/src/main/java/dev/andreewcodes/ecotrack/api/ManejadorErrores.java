package dev.andreewcodes.ecotrack.api;

import dev.andreewcodes.ecotrack.dominio.OperacionInvalidaException;
import dev.andreewcodes.ecotrack.dominio.RecursoNoEncontradoException;
import java.util.stream.Collectors;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Traduce las excepciones a respuestas RFC 9457 con un {@code detail} en español
 * que el frontend muestra tal cual.
 */
// Precedencia máxima para ganar al manejador genérico de ProblemDetail de Spring Boot.
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    ProblemDetail noEncontrado(RecursoNoEncontradoException e) {
        return problema(HttpStatus.NOT_FOUND, "No encontrado", e.getMessage());
    }

    @ExceptionHandler(OperacionInvalidaException.class)
    ProblemDetail operacionInvalida(OperacionInvalidaException e) {
        return problema(HttpStatus.CONFLICT, "Operación no permitida", e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail argumentoInvalido(IllegalArgumentException e) {
        return problema(HttpStatus.BAD_REQUEST, "Solicitud inválida", e.getMessage());
    }

    /** Errores de Bean Validation: une los mensajes de todos los campos. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacion(MethodArgumentNotValidException e) {
        String detalle = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(" "));
        return problema(HttpStatus.BAD_REQUEST, "Datos inválidos", detalle);
    }

    /** JSON mal formado o un enum con un valor que no existe. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail jsonIlegible(HttpMessageNotReadableException e) {
        return problema(HttpStatus.BAD_REQUEST, "Datos inválidos",
                "El cuerpo de la petición no es válido: revisa el tipo, la zona y los números.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ProblemDetail parametroInvalido(MethodArgumentTypeMismatchException e) {
        return problema(HttpStatus.BAD_REQUEST, "Solicitud inválida",
                "Valor no válido para '" + e.getName() + "': " + e.getValue());
    }

    private static ProblemDetail problema(HttpStatus estado, String titulo, String detalle) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(estado, detalle);
        p.setTitle(titulo);
        return p;
    }
}
