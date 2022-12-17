package com.mantas.androidlogisticsapp.model;

import java.time.LocalDateTime;
import java.util.Date;

public class DtoPostCheckpoint {
    String routeId;
    String description;
    String date;

    public DtoPostCheckpoint(String routeId, String description, String date) {
        this.routeId = routeId;
        this.description = description;
        this.date = date;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
