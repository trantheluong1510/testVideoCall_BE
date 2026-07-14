package com.videocall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private String roomName;
    private String accessToken;
    private String url;
    private String message;
}
