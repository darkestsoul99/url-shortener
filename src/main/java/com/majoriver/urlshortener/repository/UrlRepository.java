package com.majoriver.urlshortener.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.majoriver.urlshortener.model.Url;

public interface UrlRepository extends JpaRepository<Url, String> {
    void deleteByExpiresAtBefore(LocalDateTime now);
}