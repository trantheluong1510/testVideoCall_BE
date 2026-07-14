package com.videocall.service;

import com.videocall.config.LiveKitConfig;
import com.videocall.dto.CallHistory;
import com.videocall.dto.CreateRoomRequest;
import com.videocall.dto.JoinRoomRequest;
import com.videocall.dto.RoomResponse;
import io.livekit.server.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class LiveKitService {

    private final LiveKitConfig liveKitConfig;
    private final RestTemplate restTemplate = new RestTemplate();
    
    private final Map<String, CallHistory> activeCalls = new ConcurrentHashMap<>();
    private final List<CallHistory> callHistory = new ArrayList<>();

    public RoomResponse createRoom(CreateRoomRequest request) {
        try {
            // For now, just generate token - room will be created when first participant joins
            log.info("Room requested: {}", request.getRoomName());

            // Generate access token
            String token = generateToken(request.getRoomName(), request.getParticipantName());

            // Track call history
            CallHistory history = new CallHistory(
                request.getRoomName(),
                request.getParticipantName(),
                LocalDateTime.now(),
                null,
                0
            );
            activeCalls.put(request.getRoomName(), history);

            return new RoomResponse(
                request.getRoomName(),
                token,
                liveKitConfig.getUrl(),
                "Room created successfully"
            );
        } catch (Exception e) {
            log.error("Error creating room: {}", e.getMessage());
            throw new RuntimeException("Failed to create room: " + e.getMessage());
        }
    }

    public RoomResponse joinRoom(JoinRoomRequest request) {
        try {
            // Generate access token
            String token = generateToken(request.getRoomName(), request.getParticipantName());

            return new RoomResponse(
                request.getRoomName(),
                token,
                liveKitConfig.getUrl(),
                "Joined room successfully"
            );
        } catch (Exception e) {
            log.error("Error joining room: {}", e.getMessage());
            throw new RuntimeException("Failed to join room: " + e.getMessage());
        }
    }

    public String endRoom(String roomName) {
        try {
            // Update call history
            CallHistory history = activeCalls.get(roomName);
            if (history != null) {
                history.setEndTime(LocalDateTime.now());
                history.setDuration((int) java.time.Duration.between(
                    history.getStartTime(), 
                    history.getEndTime()
                ).getSeconds());
                callHistory.add(history);
                activeCalls.remove(roomName);
            }

            log.info("Room ended: {}", roomName);
            return "Room ended successfully";
        } catch (Exception e) {
            log.error("Error ending room: {}", e.getMessage());
            throw new RuntimeException("Failed to end room: " + e.getMessage());
        }
    }

    public List<CallHistory> getCallHistory() {
        return new ArrayList<>(callHistory);
    }

    private String generateToken(String roomName, String participantName) {
        AccessToken accessToken = new AccessToken(
            liveKitConfig.getApiKey(),
            liveKitConfig.getApiSecret()
        );

        accessToken.setName(participantName);
        accessToken.setIdentity(participantName + "_" + System.currentTimeMillis());

        accessToken.addGrants(
            new io.livekit.server.RoomJoin(true),
            new io.livekit.server.RoomName(roomName),
            new io.livekit.server.CanPublish(true),
            new io.livekit.server.CanSubscribe(true),
            new io.livekit.server.CanPublishData(true)
        );

        return accessToken.toJwt();
    }
}
