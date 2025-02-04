package com.project.quickstay.domain.user.dto;

import com.project.quickstay.common.Social;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegister {

    private String email;
    private Social social;
    private String nickname;
}
