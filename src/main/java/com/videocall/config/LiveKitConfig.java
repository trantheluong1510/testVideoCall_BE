package com.videocall.config;

import io.livekit.server.AccessToken;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "livekit")
public class LiveKitConfig {
    private String url;
    private String apiKey;
    private String apiSecret;
    private RoomConfig room;

    @Data
    public static class RoomConfig {
        private int defaultEmptyTimeout = 300;
        private int maxParticipants = 50;
    }

    @Bean
    public AccessToken accessToken() {
        return new AccessToken(apiKey, apiSecret);
    }
}
