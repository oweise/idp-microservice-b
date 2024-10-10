package com.consol.microserviceB.repository;

import com.consol.microserviceB.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    void deleteByUuid(String uuid);
    Message findByUuid(String uuid);
}

