package com.majoriver.urlshortener.dto;

import com.majoriver.urlshortener.validation.PatchGroup;
import com.majoriver.urlshortener.validation.PostGroup;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class UrlRequest {

    @Pattern(
            regexp = "^(https)://[^\\s/$.?#].\\S*$",
            message = "Invalid URL format. The URL must start with 'https://' and follow a valid URL structure.",
            groups = {PostGroup.class, PatchGroup.class}
    )
    private String longUrl;

    @Null(message = "Invalid parameter. ID can not be updated after creation.", groups = PatchGroup.class)
    private String id;

    @PositiveOrZero(
            message = "Invalid TTL value. Time-to-live (TTL) must be a positive value or zero for infinite.",
            groups = {PostGroup.class, PatchGroup.class}
    )
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
