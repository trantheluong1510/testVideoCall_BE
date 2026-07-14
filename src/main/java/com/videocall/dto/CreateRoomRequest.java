package com.videocall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    private String roomName;
    private String participantName;
    private boolean enableRecording = false;
}
