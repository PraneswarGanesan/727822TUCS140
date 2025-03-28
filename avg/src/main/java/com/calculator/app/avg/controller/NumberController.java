// package com.calculator.app.avg.controller;

// import com.calculator.app.avg.service.NumberService;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// import java.util.*;

// @RestController
// @RequestMapping("/numbers")
// public class NumberController {
//     private final NumberService numberService;

//     public NumberController(NumberService numberService) {
//         this.numberService = numberService;
//     }

//     @GetMapping("/{type}")
//     public ResponseEntity<Map<String, Object>> getNumbers(@PathVariable String type) {
//         Map<String, Object> result = numberService.fetchNumbers(type);
//         return ResponseEntity.ok(result);
//     }
// }

package com.calculator.app.avg.controller;

import com.calculator.app.avg.service.NumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@RestController
@RequestMapping("/numbers")
public class NumberController {
    private final NumberService numberService;

    public NumberController(NumberService numberService) {
        this.numberService = numberService;
    }

    @GetMapping("/{type}")
    public ResponseEntity<String> getNumbers(@PathVariable String type) {
        Map<String, Object> result = numberService.fetchNumbers(type);

        try {
            String jsonResponse = new ObjectMapper().writeValueAsString(result);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"error\":\"Failed to format JSON\"}");
        }
    }
}
