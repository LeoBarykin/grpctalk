package blr.demo.grpctalk;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class SomeController {

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public String searchCreate() {
        return "My first response";
    }

}
