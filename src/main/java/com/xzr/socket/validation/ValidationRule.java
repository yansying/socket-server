package com.xzr.socket.validation;

import com.xzr.socket.domain.ResultCode;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * @author xzr
 */
public enum ValidationRule {
    NOT_EMPTY((value, message) -> {
        if (value instanceof List){
            List<?> list = (List<?>) value;
          return !list.isEmpty();
        }else {
            return false;
        }
    }, "列表不能为空"),

    ALL_STRINGS((value, message) -> {
        if (value instanceof List) {
            List<?> list = (List<?>) value;
            return list.stream().allMatch(item -> item instanceof String);
       }else {
            return false;
        }
    }, "所有元素必须是 String 类型"),


    COMMAND_ID(ValidationRule::getObjectStringBiPredicate, "该命令不支持"),
    RESULT_CODE((value, message) -> value instanceof Integer && Objects.equals(value, ResultCode.SUCCESS.getCode()), "消息码不合法！"),
    ;

    private static boolean getObjectStringBiPredicate(Object value, String s) {
        if (value instanceof String) {
            return true;
//            CommandIDEnum commandIDEnum = CommandIDEnum.fromInt((String) value);
//            return commandIDEnum != null;
        } else {
            return false;
        }
    }


    private final BiPredicate<Object, String> predicate;
    @Getter
    private final String errorMessage;

    ValidationRule(BiPredicate<Object, String> predicate, String errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
    }

    public boolean test(Object value) {
        return predicate.test(value, errorMessage);
    }

}

