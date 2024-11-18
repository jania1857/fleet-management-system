package pl.jania1857.fms.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DemoController {

    @GetMapping("/driver/demo")
    public ResponseEntity<String> driverDemo(Authentication auth) {
        return ResponseEntity.ok("Hello Driver!");
    }

    @GetMapping("/manager/demo")
    public ResponseEntity<String> managerDemo(Authentication auth) {
        return ResponseEntity.ok("Hello Manager!");
    }

    @GetMapping("/admin/demo")
    public ResponseEntity<String> adminDemo(Authentication auth) {
        return ResponseEntity.ok("Hello Admin!");
    }

}
