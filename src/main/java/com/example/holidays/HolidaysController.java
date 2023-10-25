package com.example.holidays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/")
public class HolidaysController {

    private final ResourceLoader resourceLoader;
    private final HolidaysService holidaysService;

    @Autowired
    public HolidaysController(ResourceLoader resourceLoader, HolidaysService holidaysService) {
        this.resourceLoader = resourceLoader;
        this.holidaysService = holidaysService;
    }

    @GetMapping("/")
    public String index() throws IOException {
        // Load and return the content of index.html
        Resource resource = resourceLoader.getResource("classpath:static/index.html");
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

    @PostMapping("/calculate")
    public ResponseEntity<String> calculateHolidays(
            @RequestParam String targetColleague,
            @RequestParam int totalHolidayAllowance,
            @RequestParam String fileUrl) {

        String result = holidaysService.calculateHolidays(targetColleague, totalHolidayAllowance, fileUrl);
        return ResponseEntity.ok(result);
    }
}
