package stock_dashboard.stock_dashboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Map<String, Object>> handleApiError(WebClientResponseException ex) {

        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                "error", "External API error",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        ));
    }

//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<Map<String, Object>> handleNullPointer(NullPointerException ex){
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
//                "error","Symbol not found or API limit reached",
//                "message", "Alpha Vantage free tier allows 5 requests/min. Wait a moment and retry.",
//                "timestamp", LocalDateTime.now().toString()
//        ));
//    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointer(
            NullPointerException ex) {

        ex.printStackTrace();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "NullPointerException",
                        "message", ex.getMessage() == null
                                ? "Unexpected null value"
                                : ex.getMessage(),
                        "timestamp", LocalDateTime.now().toString()
                ));
    }

//    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex){
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
//                "error", "Internal server error",
//                "message", ex.getMessage(),
//                "timestamp", LocalDateTime.now().toString()
//        ));
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", "Request failed",
                "message", ex.getMessage(),   // ← now shows the real reason
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}
