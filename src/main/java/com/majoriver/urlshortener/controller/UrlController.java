package com.majoriver.urlshortener.controller;

import java.net.URI;
import java.util.List;

import com.majoriver.urlshortener.validation.PatchGroup;
import com.majoriver.urlshortener.validation.PostGroup;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.majoriver.urlshortener.model.Url;
import com.majoriver.urlshortener.service.UrlService;
import com.majoriver.urlshortener.dto.UrlRequest;
import com.majoriver.urlshortener.dto.SuccessResponse;

@RestController
public class UrlController {
    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<Url> createShortUrl(@Validated({PostGroup.class}) @RequestBody UrlRequest urlRequest) {
        logger.info("Received request to shorten URL: {}", urlRequest.getLongUrl());
        Url url = urlService.createShortUrl(urlRequest.getId(), urlRequest.getLongUrl(), urlRequest.getTtlHours());
        logger.info("Successfully created short URL with ID: {}", url.getId());
        return ResponseEntity.ok(url);
    }

    @GetMapping("/urls")
    public ResponseEntity<List<Url>> getAllShortUrls() {
        logger.info("Received request to get all short URLs");
        List<Url> urls = urlService.getAllUrls();
        logger.info("Successfully fetched {} short URLs", urls.size());
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> redirectToLongUrl(@PathVariable String id) {
        logger.info("Received redirect request for ID: {}", id);
        Url url = urlService.getUrl(id).orElseThrow(() -> new EntityNotFoundException("Redirect not found"));
        logger.info("Redirecting {} to {}", id, url.getLongUrl());
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getLongUrl()))
                .build();
    }

    @DeleteMapping("/urls")
    public ResponseEntity<Object> deleteAllShortUrls() {
        logger.info("Received request to delete all short URLs");
        urlService.deleteAllUrls();
        logger.info("Successfully deleted all short URLs");
        return ResponseEntity.ok(new SuccessResponse("Successfully deleted all short URLs"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUrl(@PathVariable String id) {
        logger.info("Received request to delete URL with ID: {}", id);
        urlService.deleteUrl(id);
        logger.info("Successfully deleted URL with ID: {}", id);
        return ResponseEntity.ok(new SuccessResponse("Successfully deleted URL with ID: " + id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Url> updateUrl(
            @PathVariable String id,
            @Validated({PatchGroup.class}) @RequestBody UrlRequest urlRequest) {
        logger.info("Received request to update URL with ID: {}", id);
        Url url = urlService.updateUrl(id, urlRequest.getLongUrl(), urlRequest.getTtlHours());
        logger.info("Successfully updated URL with ID: {}", id);
        return ResponseEntity.ok(url);
    }
}
