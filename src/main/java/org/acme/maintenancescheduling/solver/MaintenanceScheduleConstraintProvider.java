package org.acme.maintenancescheduling.solver;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import org.acme.maintenancescheduling.domain.Task;
import org.acme.maintenancescheduling.enums.Priority;
import org.acme.maintenancescheduling.enums.Status;

import static ai.timefold.solver.core.api.score.stream.Joiners.overlapping;

public class MaintenanceScheduleConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{


                prioritizeBlockerAndMajorTasks(constraintFactory),
                overlaps(constraintFactory),
                avoidWeekendAssignments(constraintFactory),
                assignReadyForDevelopmentTasks(constraintFactory),
                matchDeveloperSkills(constraintFactory),
                respectWorkingHours(constraintFactory)
        };
    }

    private Constraint overlaps(ConstraintFactory constraintFactory) {

        return constraintFactory.forEachUniquePair(Task.class,
                        overlapping(Task::getStartTime, Task::getEndTime))
                .penalize(HardSoftScore.ONE_HARD).asConstraint("overlaps");
    }

    private Constraint prioritizeBlockerAndMajorTasks(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Task.class)
                .filter(task -> task.getPriority() == Priority.BLOCKER || task.getPriority() == Priority.MAJOR)
                .penalize( HardSoftScore.ONE_HARD).asConstraint("Prioritize blocker and major tasks");
    }

    private Constraint respectWorkingHours(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Task.class)
                .filter(task -> task.getAssignment() != null)
                .filter(task -> {
                    LocalTime start = task.getAssignment().getWorkStart();
                    LocalTime end = task.getAssignment().getWorkEnd();
                    Duration estimated = task.getEstimatedTime();
                    return !start.plus(estimated).isAfter(end);
                })
                .penalize( HardSoftScore.ONE_HARD).asConstraint("Respect working hours");
    }

    private Constraint avoidWeekendAssignments(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Task.class)
                .filter(task -> task.getAssignment() != null)
                .filter(task -> {
                    LocalDate dueDate = task.getDueDate();
                    return task.getAssignment().getAvailableDays().contains(dueDate);
                })
                .penalize( HardSoftScore.ONE_HARD).asConstraint("Avoid weekend assignments");
    }

    private Constraint assignReadyForDevelopmentTasks(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Task.class)
                .filter(task -> task.getStatus() == Status.READY_FOR_DEVELOPMENT)
                .penalize( HardSoftScore.ONE_SOFT).asConstraint("Assign ready for development tasks");
    }

    private Constraint matchDeveloperSkills(ConstraintFactory constraintFactory) {
        return constraintFactory.forEach(Task.class)
                .filter(task -> task.getAssignment() != null)
                .filter(task -> task.getLabels().stream().anyMatch(label -> task.getAssignment().getSkills().contains(label)))
                .penalize( HardSoftScore.ONE_SOFT).asConstraint("Match developer skills");
    }
}
