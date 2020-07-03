package com.oneslide.common.shell;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;

public class SSHManager {
    /*library object*/
    private JSch jSch;
    private SSHConnectionInfo sshConnectionInfo;
    /*long running session,the channel is ephemeral,which can run one command at a time*/
    private Session session;




    public SSHManager(SSHConnectionInfo info) throws JSchException {
        this.jSch = new JSch();
        this.jSch.setKnownHosts("");
        this.sshConnectionInfo=info;
    }

    public void connect() throws Exception {
        SSHConnectionInfo info=this.sshConnectionInfo;
        session = jSch.getSession(info.getUserName(), info.getIp(), info.getPort());
        session.setPassword(info.getPassword());
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(info.getTimeout());
    }

    public BashShell.ShellResult executeRemoteCommand(String command) {
        BashShell.ShellResult result = new BashShell.ShellResult();

        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            // get stdout and stderr
            InputStream commandOutput = channel.getInputStream();
            InputStream errStream = ((ChannelExec) channel).getErrStream();
            // connect to session to issue command
            channel.connect();
            // reading from stdout
            StringBuilder stdoutBuilder = new StringBuilder();
            int readByte = commandOutput.read();
            while (readByte != -1) {
                stdoutBuilder.append((char) readByte);
                readByte = commandOutput.read();
            }
            result.stdout = stdoutBuilder.toString();
            //reading from stderr
            StringBuilder errBuilder = new StringBuilder();
            int errByte = errStream.read();
            while (errByte != -1) {
                errBuilder.append((char) errByte);
                errByte = errStream.read();
            }
            result.stderr = errBuilder.toString();
            // check stderr is empty
            result.isSuccess = result.stderr.equals("");
            channel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * close the long running session
     **/
    public void close() {
        session.disconnect();
    }
}