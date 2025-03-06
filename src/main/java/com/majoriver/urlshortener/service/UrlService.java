package com.majoriver.urlshortener.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.majoriver.urlshortener.model.Url;
import com.majoriver.urlshortener.repository.UrlRepository;

@Service
public class UrlService {
    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url createShortUrl(String customId, String longUrl, Integer ttlHours) {
        logger.debug("Creating short URL for: {}", longUrl);

        Url url = new Url();
        url.setLongUrl(longUrl);

        if (ttlHours != null && ttlHours > 0) {
            url.setExpiresAt(LocalDateTime.now().plusHours(ttlHours));
            logger.debug("Setting TTL of {} hours for URL", ttlHours);
        }

        if (customId != null && !customId.isEmpty()) {
            logger.debug("Attempting to use custom ID: {}", customId);
            if (urlRepository.existsById(customId)) {
                logger.warn("Custom ID {} already exists", customId);
                throw new IllegalArgumentException("Custom ID already exists");
            }
            url.setId(customId);
        }

        Url savedUrl = urlRepository.save(url);
        logger.debug("Created short URL with ID: {}", savedUrl.getId());
        return savedUrl;
    }

    public List<Url> getAllUrls() {
        logger.debug("Fetching all short URLs");
        return urlRepository.findAll();
    }

    public Optional<Url> getUrl(String id) {
        logger.debug("Fetching URL for ID: {}", id);
        return urlRepository.findById(id);
    }

    public void deleteAllUrls() {
        logger.debug("Deleting all short URLs");
        urlRepository.deleteAll();
    }

    public void deleteUrl(String id) {
        logger.debug("Deleting URL with ID: {}", id);
        if (!urlRepository.existsById(id)) {
            throw new EntityNotFoundException("URL not found");
        }
        urlRepository.deleteById(id);
    }

    @Scheduled(fixedRate = 3600000)
    public void deleteExpiredUrls() {
        logger.debug("Deleting expired URLs");
        urlRepository.deleteByExpiresAtBefore(LocalDateTime.now());
    }

    public Url updateUrl(String id, String longUrl, Integer ttlHours) {
        logger.debug("Updating URL with ID: {}", id);
        return urlRepository.findById(id)
                .map(existingUrl -> {
                    if (longUrl != null && !longUrl.trim().isEmpty()) {
                        logger.debug("Updating long URL for ID {}: {}", id, longUrl);
                        existingUrl.setLongUrl(longUrl.trim());
                    }
                    if (ttlHours != null) {
                        if (ttlHours == 0) {  // If -1 is provided, set TTL to infinite
                            logger.debug("Updating TTL to infinite for ID {}", id);
                            existingUrl.setExpiresAt(null); // Null means no expiration
                        } else if (ttlHours > 0) {
                            logger.debug("Updating TTL for ID {}: {} hours", id, ttlHours);
                            existingUrl.setExpiresAt(LocalDateTime.now().plusHours(ttlHours));
                        } else {
                            throw new IllegalArgumentException("TTL must be positive or zero for infinite");
                        }
                    }
                    return urlRepository.save(existingUrl);
                })
                .orElseThrow(() -> {
                    logger.warn("URL not found for ID: {}", id);
                    return new EntityNotFoundException("URL not found");
                });
    }
}