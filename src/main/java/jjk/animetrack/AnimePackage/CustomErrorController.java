package jjk.animetrack.AnimePackage;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // You can add logic here to log the exception or inspect the status code
        // For now, let's just return the standard error view or redirect
        return "error"; 
    }
}
