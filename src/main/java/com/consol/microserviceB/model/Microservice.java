package com.consol.microserviceB.model;

import java.io.Serializable;

public class Microservice implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        id = pId;
    }
}
