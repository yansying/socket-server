package com.xzr.socket.socketcontext;


import java.io.Serializable;
/**
 * @author xzr
 **/
public interface SocketContext extends Serializable {

    SocketAuthentication getSocketAuthentication();


    void setSocketAuthentication(SocketAuthentication authentication);
}
