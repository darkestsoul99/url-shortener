package com.majoriver.urlshortener.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.majoriver.urlshortener.model.Url;
import org.springframework.transaction.annotation.Transactional;

public interface UrlRepository extends JpaRepository<Url, String> {
    @Transactional
    void deleteAllByExpiresAtBefore(LocalDateTime now);
}