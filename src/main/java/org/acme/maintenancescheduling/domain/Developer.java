package org.acme.maintenancescheduling.domain;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public class Developer {
    @PlanningId
    private String id;
    private String name;
    private List<LocalDate> availableDays;
    private LocalTime workStart;
    private LocalTime workEnd;
    private Set<String> skills;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LocalDate> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(List<LocalDate> availableDays) {
        this.availableDays = availableDays;
    }

    public LocalTime getWorkStart() {
        return workStart;
    }

    public void setWorkStart(LocalTime workStart) {
        this.workStart = workStart;
    }

    public LocalTime getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(LocalTime workEnd) {
        this.workEnd = workEnd;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }
}
