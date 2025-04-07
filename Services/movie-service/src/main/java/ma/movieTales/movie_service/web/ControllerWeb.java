package ma.movieTales.movie_service.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ControllerWeb {

    @Value("${movie.param.x}")
    private String x;

    @Value("${movie.param.y}")
    private String y;

    @GetMapping("/params")
    public Map<String , String> getDevInfo() {
        return Map.of("x" , x , "y" , y);
    }

    @GetMapping("/greetings")
    public String getGreeting(){
        return "Hello from Movie Service";
    }
}
