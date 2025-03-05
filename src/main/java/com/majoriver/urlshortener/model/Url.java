package com.majoriver.urlshortener.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Url {
    @Id
    @Column(unique = true, nullable = false)
    private String id; // Short alphanumeric ID

    @Column(nullable = false)
    @NotEmpty(message = "Long URL cannot be empty")
    private String longUrl;

    @Column(nullable = false)
    @NotNull(message = "CreatedAt cannot be null")
    private LocalDateTime createdAt = LocalDateTime.now(); // Automatically set

    private LocalDateTime expiresAt;

    // Default constructor for JPA
    public Url() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    // Generate a short ID if not provided by the client
    @PrePersist
    public void generateId() {
        if (this.id == null) {
            // Generate a short ID using truncated UUID
            this.id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        }
    }
}