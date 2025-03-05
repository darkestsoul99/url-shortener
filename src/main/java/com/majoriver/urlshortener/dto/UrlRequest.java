package com.majoriver.urlshortener.dto;

import jakarta.validation.constraints.NotEmpty;

public class UrlRequest {

    private String longUrl;

    private String id;

    private Integer ttlHours;

    public String getId() {
        return id;
    }

    public void setId(String customId) {
        this.id = customId;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Integer getTtlHours() {
        return ttlHours;
    }

    public void setTtlHours(Integer ttlHours) {
        this.ttlHours = ttlHours;
    }
}