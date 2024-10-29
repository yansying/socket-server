package com.xzr.socket.socketcontext;




import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

/**
 * @author xzr
 **/
public interface SocketAuthentication extends Serializable {

    Socket getSocket();

    Object getDeserializeData();

    PrintWriter getWriter() ;

    BufferedReader getReader() ;

    Boolean getCertificationResults();

    String getSessionId();

}
