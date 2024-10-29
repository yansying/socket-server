package com.xzr.socket.domain;




import com.xzr.socket.validation.MultiRule;
import com.xzr.socket.validation.ValidationRule;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author xzr
 */
@Data
@Accessors(chain = true)
public class ResultVO {
    //200 成功，其他失败
    @NotNull(message = "code不能为空")
    @MultiRule(rules = ValidationRule.RESULT_CODE)
    private Integer code;
    // 响应消息
    private String messageCh;


}
