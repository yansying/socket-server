package com.xzr.socket.socketcontext;



import com.xzr.socket.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Objects;


/**
 * @author xzr
 * 每次Socket连接时，调用链相关数据
 */
public class DefaultSocketAuthentication implements SocketAuthentication{




    private static final Logger log = LoggerFactory.getLogger(DefaultSocketAuthentication.class);
    private final Socket socket;


    private final Object deserializeData;


    //处理字符消息 适用于JSON等序列化
    private  BufferedReader reader;
    private  PrintWriter writer;

    // 处理二进制消息，适用于KRYO等序列化
    private  InputStream inputStream;
    private  OutputStream outputStream;
    private Boolean certificationResults;
    private  String socketKey;


    public DefaultSocketAuthentication(Socket socket,  Object deserializeData,BufferedReader reader,PrintWriter writer) throws IOException {
        this.socket = socket;
        this.deserializeData = deserializeData;
        this.reader = reader;
        this.writer = writer;

        log.info("初始化认证对象");
    }

    public DefaultSocketAuthentication(Socket socket,  Object deserializeData,InputStream inputStream,OutputStream outputStream) throws IOException {
        this.socket = socket;
        this.deserializeData = deserializeData;
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        log.info("初始化认证对象");
    }

    public DefaultSocketAuthentication(Socket socket,  Object deserializeData,BufferedReader reader,PrintWriter writer,InputStream inputStream,OutputStream outputStream) throws IOException {
        this.socket = socket;
        this.deserializeData = deserializeData;
        this.reader = reader;
        this.writer = writer;
        this.inputStream = inputStream;
        this.outputStream = outputStream;

        log.info("初始化认证对象");
    }


    /**
     * 初始化socket密钥与认证状态
     * @param socketKey 缓存key
     */
    public void initSocketKeyAndCertificationResults(String socketKey){
        this.socketKey = socketKey;
        initCertificationResults();
    }

    /**
     * 初始化认证结果
     */
    private void initCertificationResults(){
        SocketServer bean = SpringUtils.getBean(SocketServer.class);
        SocketMetBean socketMetBean = null;
        try {
             socketMetBean = bean.getSocketMetBeanCountMap().get(socketKey);
        } catch (Exception e) {
            this.certificationResults = Boolean.FALSE;
           log.warn("初始化认证状态异常，默认标注未认证，原因：{}",e.getMessage());
        }
        this.certificationResults =  Objects.nonNull(socketMetBean);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public BufferedReader getReader() {
        return reader;
    }

    @Override
    public Boolean getCertificationResults() {
        return this.certificationResults;
    }

    @Override
    public String getSessionId() {
        return this.socketKey;
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public Object getDeserializeData() {
        return this.deserializeData;
    }



}
