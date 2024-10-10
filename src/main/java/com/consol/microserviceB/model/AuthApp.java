package com.consol.microserviceB.model;

import javax.persistence.*;

@Entity
@Table(name = "auth_app")
public class AuthApp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "remote_app_uuid")
    private String remoteAppId;

    public AuthApp() {
    }

    public AuthApp(String pUuid) {
        this.remoteAppId=pUuid;
    }

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public String getRemoteAppId() {
        return remoteAppId;
    }

    public void setRemoteAppId(String pRemoteAppId) {
        remoteAppId = pRemoteAppId;
    }
}
