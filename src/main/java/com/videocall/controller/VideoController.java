package com.videocall.controller;

import com.videocall.dto.CallHistory;
import com.videocall.dto.CreateRoomRequest;
import com.videocall.dto.JoinRoomRequest;
import com.videocall.dto.RoomResponse;
import com.videocall.service.LiveKitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "https://test-video-call-fe-yeso.vercel.app", "https://*", "http://*"})
public class VideoController {

    private final LiveKitService liveKitService;

    @PostMapping("/create-room")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody CreateRoomRequest request) {
        RoomResponse response = liveKitService.createRoom(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/join-room")
    public ResponseEntity<RoomResponse> joinRoom(@RequestBody JoinRoomRequest request) {
        RoomResponse response = liveKitService.joinRoom(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/end-room")
    public ResponseEntity<String> endRoom(@RequestParam String roomName) {
        String result = liveKitService.endRoom(roomName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<CallHistory>> getCallHistory() {
        List<CallHistory> history = liveKitService.getCallHistory();
        return ResponseEntity.ok(history);
    }
}
