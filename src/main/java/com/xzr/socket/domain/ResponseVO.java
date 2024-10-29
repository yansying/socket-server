package com.xzr.socket.domain;

import cn.hutool.core.bean.BeanUtil;

import com.xzr.socket.ano.AuthenticationFailure;
import com.xzr.socket.ano.SocketMessage;
import com.xzr.socket.ano.SocketUrl;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author xzr
 * 对接C++协议
 */
@Data
@Accessors(chain = true)
@SocketMessage
public class ResponseVO implements  Serializable {
    // 操作内容数据
    private Object body;
    // 消息头
    @Valid
    @NotNull(message = "消息头不能为null")
    private HeaderVO header;
    //消息体
    @Valid
    @NotNull(message = "消息体不能为null")
    private ResultVO result;





    public static ResponseVO createResponseVO(ResponseVO responseVO,
                                              int resultCode,
                                                String resultMessageCh

    ) {

        return createResponseVO(responseVO,resultCode,resultMessageCh,null,null);
    }


    public static ResponseVO createResponseVO(ResponseVO responseVO,
                                              int resultCode,
                                              String resultMessageCh,
                                              Object body

    ) {

        return createResponseVO(responseVO,resultCode,resultMessageCh,body,null);
    }

    public static ResponseVO createResponseVO(  int resultCode,
                                                String commandId,
                                                String eqpId,
                                                String resultMessageCh,
                                                Object body){
        ResponseVO vo = new ResponseVO();
        HeaderVO headerVO = new HeaderVO().setDate(new Date()).setCommandId(commandId).setEqpId(eqpId);

        vo.setHeader(headerVO);
        ResultVO resultVO = new ResultVO().setCode(resultCode).setMessageCh(resultMessageCh);
        vo.setResult(resultVO);
        vo.setBody(body);
        return vo;
    }

    public static ResponseVO createResponseVO(ResponseVO responseVO,
                                               int resultCode,
                                               String resultMessageCh,
                                               Object body,
                                              String clientId
    ) {
        ResponseVO vo = new ResponseVO();
        BeanUtil.copyProperties(responseVO,vo);
        vo.getHeader().setDate(new Date());
        if (Objects.nonNull(clientId)){
            vo.getHeader().setSessionId(clientId);
        }
        vo.getResult().setCode(resultCode).setMessageCh(resultMessageCh);
        if (Objects.nonNull(body)){
            vo.setBody(body);
        }
        return vo;
    }

    public static ResponseVO authenticationSuccessful(ResponseVO responseVO,String clientId) {

        return   createResponseVO(responseVO,ResultCode.SUCCESS.getCode(),ResultMessageCh.AUTHENTICATION_SUCCESSFUL,null,clientId);

    }

    public static ResponseVO authenticationError(ResponseVO responseVO) {
      return   createResponseVO(responseVO,ResultCode.ERROR.getCode(),ResultMessageCh.AUTHENTICATION_ERROR);

    }

    public static ResponseVO parameterIsIllegal(ResponseVO responseVO, Map<String, String> errors) {

        return  createResponseVO(responseVO,ResultCode.ERROR.getCode(),ResultMessageCh.PARAMETER_IS_ILLEGAL,errors);

    }



//    @Override
//    public String getRequestUrl() {
//        return header.getCommandId();
//    }




//    @Override
    public ResponseVO authenticationFailureMessage(Object communicationParent) {
        ResponseVO responseVO =(ResponseVO) communicationParent;
        ResponseVO responseVO1 = authenticationError(responseVO);
        responseVO1.getResult().setMessageCh("认证失败");

        return responseVO1;
    }

    @AuthenticationFailure
    public  ResponseVO authenticationFailureMessage(ResponseVO responseVO) {
        return createResponseVO(responseVO,ResultCode.ERROR.getCode(),ResultMessageCh.SOCKET_INVALID);
    }
}
