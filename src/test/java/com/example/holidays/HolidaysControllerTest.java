package com.example.holidays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class HolidaysControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HolidaysService holidaysService;

    @Test
    public void testCalculateHolidaysWithNoHolidaysTaken() throws Exception {
        // Assign
        String colleagueName = "John";
        int totalAllowance = 20;
        String fileUrl = "path/to/calendar.ics";
        String expectedResult = "Yearly holiday allowance: 20 days\nColleague has taken 0 days of holiday\nColleague has 20 days remaining";

        when(holidaysService.calculateHolidays(colleagueName, totalAllowance, fileUrl)).thenReturn(expectedResult);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
                        .param("colleagueName", colleagueName)
                        .param("totalAllowance", String.valueOf(totalAllowance))
                        .param("fileUrl", fileUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testCalculateHolidaysWithSomeHolidaysTaken() throws Exception {
        // Assign
        String colleagueName = "Alice";
        int totalAllowance = 20;
        String fileUrl = "path/to/calendar.ics";
        String expectedResult = "Yearly holiday allowance: 20 days\nColleague has taken 10 days of holiday\nColleague has 10 days remaining";

        when(holidaysService.calculateHolidays(colleagueName, totalAllowance, fileUrl)).thenReturn(expectedResult);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
                        .param("colleagueName", colleagueName)
                        .param("totalAllowance", String.valueOf(totalAllowance))
                        .param("fileUrl", fileUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testCalculateHolidaysWithMaximumAllowableHolidays() throws Exception {
        // Assign
        String colleagueName = "Bob";
        int totalAllowance = 30;
        String fileUrl = "path/to/calendar.ics";
        String expectedResult = "Yearly holiday allowance: 30 days\nColleague has taken 30 days of holiday\nColleague has 0 days remaining";

        when(holidaysService.calculateHolidays(colleagueName, totalAllowance, fileUrl)).thenReturn(expectedResult);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
                        .param("colleagueName", colleagueName)
                        .param("totalAllowance", String.valueOf(totalAllowance))
                        .param("fileUrl", fileUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testCalculateHolidaysWithInvalidInputs() throws Exception {
        // Assign
        String colleagueName = "InvalidColleague";
        int totalAllowance = -5;  // Negative total allowance
        String fileUrl = "invalid-url";  // Invalid URL
        String expectedResult = "Error occurred while processing holidays.";

        when(holidaysService.calculateHolidays(colleagueName, totalAllowance, fileUrl)).thenReturn(expectedResult);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
                        .param("colleagueName", colleagueName)
                        .param("totalAllowance", String.valueOf(totalAllowance))
                        .param("fileUrl", fileUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testCalculateHolidaysWithEdgeCases() throws Exception {
        // Assign
        String colleagueName = "EdgeCaseColleague";
        int totalAllowance = 1;  // Minimum total allowance
        String fileUrl = "path/to/calendar.ics";
        String expectedResult = "Yearly holiday allowance: 1 days\nColleague has taken 0 days of holiday\nColleague has 1 days remaining";

        when(holidaysService.calculateHolidays(colleagueName, totalAllowance, fileUrl)).thenReturn(expectedResult);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
                        .param("colleagueName", colleagueName)
                        .param("totalAllowance", String.valueOf(totalAllowance))
                        .param("fileUrl", fileUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }

    @Test
    public void testCalculateHolidaysWithFileNotFound() throws Exception {
        // Assign
        String colleagueName = "FileNotFoundColleague";
        int totalAllowance = 20;
        String fileUrl = "non-existent-file.ics";
        String expectedResult = "An error occurred: File not found.";

        when(holidaysService.calculateHolidays(colleagueName, totalAllowance, fileUrl)).thenReturn(expectedResult);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/calculate")
                        .param("colleagueName", colleagueName)
                        .param("totalAllowance", String.valueOf(totalAllowance))
                        .param("fileUrl", fileUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResult));
    }
}

