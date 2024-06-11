package org.acme.maintenancescheduling.rest;

import static io.restassured.RestAssured.given;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.maintenancescheduling.domain.Developer;
import org.acme.maintenancescheduling.domain.MaintenanceSchedule;
import org.acme.maintenancescheduling.domain.Task;
import org.acme.maintenancescheduling.enums.*;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class MaintenanceScheduleResourceTest {

    @Test
    public void testSolveEndpoint() {
        // Create an example MaintenanceSchedule object
        MaintenanceSchedule schedule = new MaintenanceSchedule();
        // Add some example tasks and developers
        schedule.setTaskList(createExampleTasks());
        schedule.setDeveloperList(createExampleDevelopers());

        // Send the schedule to the solve endpoint
        given()
                .contentType("application/json")
                .body(schedule)
                .when().post("/schedule")
                .then()
                .statusCode(202);
    }

    private List<Task> createExampleTasks() {
        // Create some example tasks
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setId("1");
        task1.setType(Type.BUG);
        task1.setPriority(Priority.BLOCKER);
        task1.setStatus(Status.READY_FOR_DEVELOPMENT);
        task1.setEstimatedTime(Duration.ofHours(3));
        task1.setResolution(Resolution.UNRESOLVED);
        task1.setLabels(Arrays.asList("frontend"));
        task1.setDueDate(LocalDate.now());
        tasks.add(task1);

        // Add more tasks as needed
        return tasks;
    }

    private List<Developer> createExampleDevelopers() {
        // Create some example developers
        List<Developer> developers = new ArrayList<>();
        Developer dev1 = new Developer();
        dev1.setName("John Doe");
        dev1.setAvailableDays(Arrays.asList(LocalDate.now()));
        dev1.setWorkStart(LocalTime.of(9, 0));
        dev1.setWorkEnd(LocalTime.of(17, 0));
        developers.add(dev1);

        // Add more developers as needed
        return developers;
    }
}
