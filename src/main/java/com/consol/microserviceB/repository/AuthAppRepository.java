package com.consol.microserviceB.repository;

import com.consol.microserviceB.model.AuthApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthAppRepository extends JpaRepository<AuthApp, Long> {
    AuthApp getAuthAppByRemoteAppId(String remoteAppId);
}
