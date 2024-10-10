package com.consol.microserviceB.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tasks")
public class TaskModel implements Serializable {

    public enum TaskStatus {
        CREATED, PROCESS, CLOSED, REJECTED, UPDATED
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "task_status")
    private TaskStatus status;

    @Column(name="appId")
    String microserviceAId;

    @Column(name="remoteAppId")
    String microserviceBId;

    @Column(name="creationDate")
    long creationDate;

    @Column(name="updateDate")
    long updateDate;

    @Column
    String uuid;

    public long getId() {
        return id;
    }

    public void setId(long pId) {
        id = pId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus pStatus) {
        status = pStatus;
    }

    public String getMicroserviceAId() {
        return microserviceAId;
    }

    public void setMicroserviceAId(String pMicroserviceAId) {
        microserviceAId = pMicroserviceAId;
    }

    public String getMicroserviceBId() {
        return microserviceBId;
    }

    public void setMicroserviceBId(String pMicroserviceBId) {
        microserviceBId = pMicroserviceBId;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long pCreationDate) {
        creationDate = pCreationDate;
    }

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long pUpdateDate) {
        updateDate = pUpdateDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String pUuid) {
        uuid = pUuid;
    }
}
