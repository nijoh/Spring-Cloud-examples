package com.example.nijo.common.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;

    private String userName;

    private String email;

    private String password;
}
