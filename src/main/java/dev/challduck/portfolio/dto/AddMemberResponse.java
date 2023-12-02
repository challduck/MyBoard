package dev.challduck.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddMemberResponse {
    private int status;
    private String message;
}
