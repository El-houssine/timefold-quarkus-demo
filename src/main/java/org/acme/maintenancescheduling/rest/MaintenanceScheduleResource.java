package org.acme.maintenancescheduling.rest;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.solver.SolverManager;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.acme.maintenancescheduling.domain.MaintenanceSchedule;
import org.jboss.logging.Logger;

@Path("/schedule")
public class MaintenanceScheduleResource {

    private static final Logger logger = Logger.getLogger(MaintenanceScheduleResource.class.getName());

    @Inject
    SolverManager<MaintenanceSchedule, Long> solverManager;

    private MaintenanceSchedule latestSolution;

    @POST
    public Response solve(MaintenanceSchedule problem) {
        logger.info("Received problem to solve: " + problem);

        if (problem.getDeveloperList() == null || problem.getDeveloperList().isEmpty()) {
            logger.error("Developer list cannot be null or empty");
            return Response.status(Response.Status.BAD_REQUEST).entity("Developer list cannot be null or empty").build();
        }
        if (problem.getTaskList() == null || problem.getTaskList().isEmpty()) {
            logger.error("Task list cannot be null or empty");
            return Response.status(Response.Status.BAD_REQUEST).entity("Task list cannot be null or empty").build();
        }
        if (problem.getScore() == null) {
            problem.setScore(HardSoftScore.ZERO); // Initialize score if null
        }

        solverManager.solve(1L, id -> problem, this::onSolution);
        return Response.accepted().build();
    }

    @GET
    @Path("/solution")
    public MaintenanceSchedule getSolution() {
        return latestSolution;
    }

    private void onSolution(MaintenanceSchedule solution) {
        logger.info("Solution found!");
        this.latestSolution = solution;
    }
}
