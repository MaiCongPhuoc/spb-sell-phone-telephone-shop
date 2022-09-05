package com.cg.uploader;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
//import org.springframework.boot.context.properties.ConfigurationProperties;

//@ConfigurationProperties(prefix = "application.uploader")
@Data
@Component
public class CloudinaryConfig {
    @Value("${application.uploader.cloud-name}")
    private String cloudName;
    @Value("${application.uploader.api-key}")
    private String apiKey;
    @Value("${application.uploader.api-secret}")
    private String apiSecret;

}