package org.acme.maintenancescheduling.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WorkCalendar {

    private List<Developer> developerList;

    public WorkCalendar(List<Developer> developerList) {
        this.developerList = developerList;
    }

    public List<LocalDateTime> generateTimeSlots() {
        List<LocalDateTime> timeSlots = new ArrayList<>();
        for (Developer developer : developerList) {
            for (LocalDate day : developer.getAvailableDays()) {
                LocalDate date = LocalDate.parse(day.toString());
                LocalTime startTime = developer.getWorkStart();
                LocalTime endTime = developer.getWorkEnd();
                while (startTime.isBefore(endTime)) {
                    timeSlots.add(LocalDateTime.of(date, startTime));
                    startTime = startTime.plusHours(1); // Assuming 1-hour slots
                }
            }
        }
        return timeSlots;
    }
}
