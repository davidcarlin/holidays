package com.example.holidays;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class HolidaysServiceImpl implements HolidaysService {

    @Override
    public String calculateHolidays(String colleagueName, int totalAllowance, String fileUrl) {
        try {
            String calendarData = readFile(fileUrl);

            String[] events = calendarData.split("BEGIN:VEVENT");

            int daysTaken = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date holidayYearStart;
            Date holidayYearEnd;
            try {
                holidayYearStart = sdf.parse("20230401"); // April 1, 2023
                holidayYearEnd = sdf.parse("20240331");   // March 31, 2024
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing date: " + e.getMessage());
            }

            for (String event : events) {
                if (event.contains(colleagueName) && event.contains("CATEGORIES:leaves")) {
                    String[] lines = event.split("\n");
                    String startDateStr = null;
                    String endDateStr = null;

                    for (String line : lines) {
                        if (line.startsWith("DTSTART;VALUE=DATE:")) {
                            startDateStr = line.substring(19);
                        } else if (line.startsWith("DTEND;VALUE=DATE:")) {
                            endDateStr = line.substring(17);
                        }
                    }

                    if (startDateStr != null && endDateStr != null) {
                        try {
                            Date startDate = sdf.parse(startDateStr);
                            Date endDate = sdf.parse(endDateStr);
                            if (isWithinHolidayYear(startDate, holidayYearStart, holidayYearEnd)) {
                                int daysDiff = calculateWeekdays(startDate, endDate);
                                daysTaken += daysDiff;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            int daysRemaining = totalAllowance - daysTaken;

            String result = "Yearly holiday allowance: " + totalAllowance + " days\n"
                    + "Colleague has taken " + daysTaken + " days of holiday\n"
                    + "Colleague has " + daysRemaining + " days remaining";

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while processing holidays.";
        }
    }

    private boolean isWithinHolidayYear(Date date, Date holidayYearStart, Date holidayYearEnd) {
        return date.after(holidayYearStart) && date.before(holidayYearEnd);
    }

    private int calculateWeekdays(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int weekdays = 0;
        while (!startCal.after(endCal)) {
            int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
            if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY) {
                weekdays++;
            }
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.getTime().compareTo(endDate) == 0) {
                break;
            }
        }
        return weekdays;
    }

    public String readFile(String path) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
}
