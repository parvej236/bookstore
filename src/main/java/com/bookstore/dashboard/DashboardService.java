package com.bookstore.dashboard;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DashboardService {
    public List<String> getLast7Days() {
        List<String> daysList = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 1; i <= 7; i++) {
            LocalDate pastDate = today.minusDays(i);
            String dayName = pastDate.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            daysList.add(dayName);
        }

        List<String> orderedList = new ArrayList<>();
        for (int i = daysList.size() - 1; i >= 0; i--) {
            orderedList.add(daysList.get(i));
        }
        return orderedList;
    }
}
