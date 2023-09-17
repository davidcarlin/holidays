package com.example.holidays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class HolidaysController {

    private final HolidaysService holidaysService;

    @Autowired
    public HolidaysController(HolidaysService holidaysService) {
        this.holidaysService = holidaysService;
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
