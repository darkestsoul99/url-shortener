package com.majoriver.urlshortener.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.majoriver.urlshortener.model.Url;
import com.majoriver.urlshortener.service.UrlService;
import com.majoriver.urlshortener.dto.UrlRequest;

import jakarta.validation.Valid;

@RestController
@Validated
public class UrlController {
    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> createShortUrl(@Valid @RequestBody UrlRequest urlRequest) {
        logger.info("Received request to shorten URL: {}", urlRequest.getLongUrl());
        try {
            Url url = urlService.createShortUrl(urlRequest.getId(), urlRequest.getLongUrl(), urlRequest.getTtlHours());
            logger.info("Successfully created short URL with ID: {}", url.getId());
            return ResponseEntity.ok(url);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to create short URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/urls")
    public ResponseEntity<?> getAllShortUrls() {
        logger.info("Received request to get all short URLs");
        try {
            List<Url> urls = urlService.getAllUrls();
            logger.info("Successfully fetched {} short URLs", urls.size());
            return ResponseEntity.ok(urls);
        } catch (Exception e) {
            logger.error("Failed to fetch short URLs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch short URLs");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> redirectToLongUrl(@PathVariable String id) {
        logger.info("Received redirect request for ID: {}", id);
        return urlService.getUrl(id)
                .map(url -> {
                    logger.info("Redirecting {} to {}", id, url.getLongUrl());
                    return ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create(url.getLongUrl()))
                            .build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Redirect not found"));
    }

    @DeleteMapping("/urls")
    public ResponseEntity<?> deleteAllShortUrls() {
        logger.info("Received request to delete all short URLs");
        try {
            urlService.deleteAllUrls();
            logger.info("Successfully deleted all short URLs");
            return ResponseEntity.ok("All URLs deleted successfully");
        } catch (Exception e) {
            logger.error("Failed to delete all short URLs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete all URLs");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUrl(@PathVariable String id) {
        logger.info("Received request to delete URL with ID: {}", id);
        try {
            urlService.deleteUrl(id);
            logger.info("Successfully deleted URL with ID: {}", id);
            return ResponseEntity.ok("Successfully deleted URL with ID: " + id);
        } catch (Exception e) {
            logger.warn("Failed to delete URL with ID: {} : {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete URL with ID: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUrl(
            @PathVariable String id,
            @Valid @RequestBody UrlRequest urlRequest) {
        logger.info("Received request to update URL with ID: {}", id);


        try {
            Url url = urlService.updateUrl(id, urlRequest.getLongUrl(), urlRequest.getTtlHours());
            logger.info("Successfully updated URL with ID: {}", id);
            return ResponseEntity.ok(url);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to update URL: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}