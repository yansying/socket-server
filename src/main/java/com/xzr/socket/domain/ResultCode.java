package com.xzr.socket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xzr
 */
@Getter
@AllArgsConstructor
public  enum ResultCode {
        SUCCESS(200),
        ERROR(-200);
        private final Integer code;
}
