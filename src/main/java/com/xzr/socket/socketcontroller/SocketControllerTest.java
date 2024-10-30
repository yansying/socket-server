package com.xzr.socket.socketcontroller;


import com.xzr.socket.ano.*;
import com.xzr.socket.domain.ResponseVO;
import com.xzr.socket.socketcontext.SocketAuthentication;
import com.xzr.socket.socketcontext.SocketContextHolder;
import com.xzr.socket.socketcontext.SocketMetBean;
import com.xzr.socket.socketcontext.SocketServer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ConcurrentMap;

@SocketController
@RequiredArgsConstructor
public class SocketControllerTest {
    private static final Logger log = LoggerFactory.getLogger(SocketControllerTest.class);
    private final SocketServer socketServer;
    /**
     * 登录认证
     * @return
     */
    @SocketMapping("1001")
    public ResponseVO test(@SocketRequestData ResponseVO responseVO,
                                    @SocketContextWriter PrintWriter writer,
                                    @SocketContextReader BufferedReader reader,
                                    @SocketContextKey String sessionId,
                                    @SocketContextSocket Socket socket,
                                    @SocketRequestData("header.date") Date date
                                    ){
        log.info("date:{}",date.toString());
        ConcurrentMap<String, SocketMetBean> socketMetBeanCountMap = socketServer.getSocketMetBeanCountMap();
        socketMetBeanCountMap.put(sessionId, new SocketMetBean(
                socket,writer,reader
        ));
        responseVO.getHeader().setDate(new Date());
        responseVO.getResult().setMessageCh("认证成功");
        return responseVO;
    }

    @SocketMapping("1003")
    public ResponseVO test1003(@SocketRequestData ResponseVO responseVO) {
        responseVO.getHeader().setDate(new Date());
        responseVO.getResult().setMessageCh("1003测试成功");
        return responseVO;
    }
}
