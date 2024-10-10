package com.consol.microserviceB.sender;

import com.consol.microserviceB.MicroserviceBApplication;
import com.consol.microserviceB.model.TaskModel;
import com.consol.microserviceB.receiver.Receiver;
import com.consol.microserviceB.repository.AuthAppRepository;
import com.consol.microserviceB.repository.TaskRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class Sender {
    private static final Logger log = LoggerFactory.getLogger(Sender.class);
    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    @Autowired
    AuthAppRepository authAppRepository;
    @Autowired
    TaskRepository taskRepository;
    @Value("${random.uuid}")
    private UUID appId;
    private Random rand = new Random();

    public Sender(RabbitTemplate pRabbitTemplate, Receiver pReceiver) {
        rabbitTemplate = pRabbitTemplate;
        receiver = pReceiver;
    }

    public void send() throws Exception {
        String remoteUuid = authAppRepository.findAll().stream().findAny().orElseThrow(() -> new RuntimeException()).getRemoteAppId();
        taskRepository.findByStatus(TaskModel.TaskStatus.REJECTED).forEach(taskToSend -> {
            taskToSend.setMicroserviceAId(appId.toString());
            taskToSend.setMicroserviceBId(remoteUuid);
            taskToSend.setUpdateDate(System.currentTimeMillis());
            taskToSend.setStatus(TaskModel.TaskStatus.UPDATED);
            String jsonInString = new Gson().toJson(taskToSend);
            int sendProbability = rand.nextInt(100);
            if (sendProbability < 50) {
                rabbitTemplate.convertAndSend(MicroserviceBApplication.topicExchangeName, "microserviceA", jsonInString);
                log.info("RABBITMQ Task sent: " + jsonInString);
            }
        });
    }
}
