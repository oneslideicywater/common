package com.oneslide.common.shell;

public class SSHConnectionInfo {

    private final String userName;
    private final String password;
    private final String ip;
    private final int port;
    private final int timeout;

    public SSHConnectionInfo(String userName, String password, String ip, int port, int timeout) {
        this.userName = userName;
        this.password = password;
        this.ip = ip;
        this.port = port;
        this.timeout = timeout;
    }
    public SSHConnectionInfo(String userName, String password, String ip){
        this(userName,password,ip,22,60000);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }
}
