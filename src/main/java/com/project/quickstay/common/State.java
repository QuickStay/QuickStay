package com.project.quickstay.common;

import lombok.Getter;

@Getter
public enum State {
    RESERVED("예약중"),
    CANCELLED("예약 취소"),
    COMPLETED("이용 완료");

    // 문자열을 저장할 필드
    private final String state;

    // 생성자 (싱글톤)
    private State(String state) {
        this.state = state;
    }
}
