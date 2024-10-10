package com.consol.microserviceB.receiver;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.consol.microserviceB.model.TaskModel;
import com.consol.microserviceB.repository.TaskRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @Autowired
    TaskRepository taskRepository;

    private Random rand = new Random();

    private Logger log = LoggerFactory.getLogger(Receiver.class);

    @RabbitListener(queues = "microserviceB")
    public void handleMessage(String pTask) {
        TaskModel receivedTask = new Gson().fromJson(pTask, TaskModel.class);
        log.info("RABBITMQ Received task: " + pTask);
        int taskStatus = rand.nextInt(2) + 2;
        if(receivedTask.getStatus() == TaskModel.TaskStatus.UPDATED){
            receivedTask.setStatus(TaskModel.TaskStatus.CLOSED);
            log.info("RABBITMQ Set Task status: " + receivedTask.getStatus());
        } else {
            receivedTask.setStatus(TaskModel.TaskStatus.values()[taskStatus]);
            log.info("RABBITMQ Set Task status: " + receivedTask.getStatus());
        }
        taskRepository.save(receivedTask);
    }

}