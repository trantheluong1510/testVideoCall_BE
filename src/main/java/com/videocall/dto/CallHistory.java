package com.videocall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallHistory {
    private String roomName;
    private String participantName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration;
}
