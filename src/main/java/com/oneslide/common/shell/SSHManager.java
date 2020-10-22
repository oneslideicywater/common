package com.oneslide.common.shell;

import com.jcraft.jsch.*;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

import static net.sf.expectit.matcher.Matchers.regexp;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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

    public static void main(String[] args) throws Exception {
        //192.168.10.8
        SSHConnectionInfo connectionInfo=new SSHConnectionInfo("root","icywater",
                "10.15.0.32",22,60000);
        SSHManager manager=new SSHManager(connectionInfo);
        manager.connect();
        Channel channel= manager.getSession().openChannel("shell");
        channel.connect(3*1000);
        Scanner scanner=new Scanner(System.in);
        String temp=scanner.nextLine();
        while (!temp.equals("exit")){
            temp=scanner.nextLine();
            if (temp.equals("") || temp.trim().equals("") ||temp.startsWith("cd")){
            }else {
                temp=temp+"|sed -r \"s/\\x1B\\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]//g\"";
            }

            String result=BashShell.interactiveExecRemoteCommand(channel,temp.getBytes(),true);
            result=result.substring(result.indexOf("\r\n")+2);
            System.out.print(result);
        }
/*"s/\x1B\[([0-9]{1,2}(;[0-9]{1,2})?)?[m|K]//g"*/

        channel.disconnect();
        manager.close();
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}