package com.project.quickstay;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class CleanUp {

    @Autowired
    EntityManager em;

    private final List<String> tableNames = List.of("batch_job_execution",
            "batch_job_execution_context",
            "batch_job_execution_params",
            "batch_job_instance",
            "batch_step_execution",
            "batch_step_execution_context",
            "booking",
            "day_booking",
            "place",
            "reservation",
            "review",
            "room",
            "time_booking",
            "users");

    @Transactional
    public void execute() {
        em.flush();
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
//            em.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }

        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

    }
}