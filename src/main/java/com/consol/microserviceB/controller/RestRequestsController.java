package com.consol.microserviceB.controller;

import com.consol.microserviceB.model.*;
import com.consol.microserviceB.repository.AuthAppRepository;
import com.consol.microserviceB.repository.MessageRepository;
import com.consol.microserviceB.repository.TaskRepository;
import com.consol.microserviceB.sender.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
public class RestRequestsController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    Sender sender;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AuthAppRepository authAppRepository;

    @Value("${random.uuid}")
    private UUID appId;

    private Logger log = LoggerFactory.getLogger(RestRequestsController.class);

   @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Microservice microservice) {
        log.info("REST Register request for app id: " + microservice.getId());

        if(microservice != null) {
            AuthApp authApp = authAppRepository.getAuthAppByRemoteAppId(microservice.getId());
            if(authApp == null) {
                authAppRepository.save(new AuthApp(microservice.getId()));
                log.info("REST Registered app id: " + microservice.getId());
                return ResponseEntity.ok("Registered properly");
            } else {
                log.info("REST App with id: " + microservice.getId() + "exists");
                return ResponseEntity.ok("App with id: " + microservice.getId() + " exists");
            }
        } else {
            return ResponseEntity.status(422).body("Argument cannot be null");
        }
    }

    @PostMapping("/request")
    public ResponseEntity<Message> createRequest(@RequestBody Message pMessage) {
        log.info("REST Request: POST, message uuid: " + pMessage.getUuid());
        try {
            Message message = messageRepository.save(pMessage);
            log.info("REST Successfully saved message with uuid: " + pMessage.getUuid());
            return ResponseEntity.ok(message);
        } catch(Exception e) {
            log.error("REST Error during saving message with uuid: " + pMessage.getUuid());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/request/{bodyId}")
    public ResponseEntity<Message> updateRequest(@RequestBody Message pMessage, @PathVariable Long bodyId) {
        return messageRepository.findById(bodyId).map(body -> {
            body.setName(pMessage.getName());
            return ResponseEntity.ok(messageRepository.save(body));
        }).orElseGet(() -> ResponseEntity.ok(messageRepository.save(pMessage)));
    }

    @Transactional
    @DeleteMapping("/request/{uuid}")
    public void deleteRequest(@PathVariable String uuid) throws InterruptedException {
        try {
            log.info("REST Request REMOVE, message uuid: " + uuid);
            messageRepository.deleteByUuid(uuid);
            log.info("REST Successfully removed message with uuid: " + uuid);
        } catch (Exception ex) {
            log.error("REST Message with uuid: " + uuid + " deletion failed!");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Deletion failed message with uuid: " + uuid, ex);
        }

    }

    @GetMapping("/request")
    public ResponseEntity<List<Message>> getAll() {
        return ResponseEntity.ok(messageRepository.findAll());
    }

    @GetMapping("/request/{uuid}")
    public ResponseEntity<Message> getOne(@PathVariable String uuid) {
        try {
            log.info("REST Request GET, path: /request/" + uuid);
            Message message = messageRepository.findByUuid(uuid);
            log.info("REST Successfully get message with uuid: " + message.getUuid());
            return ResponseEntity.ok(message);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Getting failed message with uuid: " + uuid, ex);
        }
    }

    @Scheduled(fixedRate = 20000)
    public void messagingSystem() {
        try {
            sender.send();
        } catch (Exception pE) {
            log.error("RABBITMQ Exception during sending Task");
        }
    }
}
