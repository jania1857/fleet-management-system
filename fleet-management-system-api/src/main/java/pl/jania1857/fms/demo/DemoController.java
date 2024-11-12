package pl.jania1857.fms.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping("/driver")
    public ResponseEntity<String> driverDemo() {
        return ResponseEntity.ok("Hello Driver!");
    }

    @GetMapping("/manager")
    public ResponseEntity<String> managerDemo() {
        return ResponseEntity.ok("Hello Manager!");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> adminDemo() {
        return ResponseEntity.ok("Hello Admin!");
    }

}
