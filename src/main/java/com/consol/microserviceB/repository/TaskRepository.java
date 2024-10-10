package com.consol.microserviceB.repository;

import com.consol.microserviceB.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    List<TaskModel> findByStatus(TaskModel.TaskStatus pStatus);
    void removeByStatus(TaskModel.TaskStatus pStatus);
}
