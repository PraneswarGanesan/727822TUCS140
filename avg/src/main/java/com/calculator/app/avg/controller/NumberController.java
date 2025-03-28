package com.calculator.app.avg.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.calculator.app.avg.service.*;
@RestController
@RequestMapping("/numbers")
public class NumberController {
    private final NumberService numberService;

    public NumberController(NumberService numberService) {
        this.numberService = numberService;
    }

    @GetMapping("/{type}")
    public ResponseEntity<Map<String, Object>> getNumbers(
            @PathVariable String type,
            @RequestHeader("Authorization") String token) {
        
        Map<String, Object> result = numberService.fetchNumbers(type, token);
        return ResponseEntity.ok(result);
    }
}
