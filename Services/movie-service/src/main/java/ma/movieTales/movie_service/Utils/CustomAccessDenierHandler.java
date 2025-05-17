package ma.movieTales.movie_service.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.entity.ContentType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDenierHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorResponse error = new ErrorResponse("Invalid or expired Token" , "Forbidden" , HttpServletResponse.SC_FORBIDDEN , LocalDateTime.now());
        new ObjectMapper().writeValue(response.getOutputStream() , error);
    }
}
