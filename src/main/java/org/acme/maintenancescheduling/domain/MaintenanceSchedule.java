package org.acme.maintenancescheduling.domain;

import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;

@PlanningSolution
public class MaintenanceSchedule {

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "developerRange")
    private List<Developer> developerList;

    @PlanningEntityCollectionProperty
    private List<Task> taskList;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "timeSlotRange")
    private List<LocalDateTime> timeSlotList;

    @PlanningScore
    private HardSoftScore score;

    public MaintenanceSchedule() {
    }

    public MaintenanceSchedule(List<Developer> developerList, List<Task> taskList) {
        this.developerList = developerList;
        this.taskList = taskList;
        this.timeSlotList = new WorkCalendar(developerList).generateTimeSlots();
    }

    public List<Developer> getDeveloperList() {
        return developerList;
    }

    public void setDeveloperList(List<Developer> developerList) {
        this.developerList = developerList;
        this.timeSlotList = new WorkCalendar(developerList).generateTimeSlots(); // Update time slots when developer list changes
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
    @JsonIgnore
    public List<LocalDateTime> getTimeSlotList() {
        return timeSlotList;
    }

    public void setTimeSlotList(List<LocalDateTime> timeSlotList) {
        this.timeSlotList = timeSlotList;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }
}
