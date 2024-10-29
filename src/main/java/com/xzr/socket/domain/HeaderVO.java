package com.xzr.socket.domain;



import com.xzr.socket.ano.SocketUrl;
import com.xzr.socket.validation.MultiRule;
import com.xzr.socket.validation.ValidationRule;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author xzr
 */
@Data
@Accessors(chain = true)
public class HeaderVO  {
    // 日期时间
    @NotNull(message = "日期时间不能为空")
    private Date date;
    // 命令ID
    @NotBlank(message = "命令ID不能为空")
    @MultiRule(rules = ValidationRule.COMMAND_ID)
    @SocketUrl
    private String commandId;
    // 设备码id
    @NotBlank(message = "设备码不能为空")
    private String eqpId;
    // 会话ID
    private String sessionId;
}
