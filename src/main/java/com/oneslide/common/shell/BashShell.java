package com.oneslide.common.shell;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;

/**
 * bash shell command utils,currently only support windows and linux,if the platform is others
 * which is judge by jvm runtime,you'll get a "linux suites" for default.
 **/
public class BashShell {
    public static class ShellResult {
        String stderr;
        String stdout;
        boolean isSuccess;

        public String getStderr() {
            return stderr;
        }

        public void setStderr(String stderr) {
            this.stderr = stderr;
        }

        public String getStdout() {
            return stdout;
        }

        public void setStdout(String stdout) {
            this.stdout = stdout;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public void setSuccess(boolean success) {
            isSuccess = success;
        }

        @Override
        public String toString() {
            return "ShellResult{" +
                    "stderr='" + stderr + '\'' +
                    ", stdout='" + stdout + '\'' +
                    ", isSuccess=" + isSuccess +
                    '}';
        }
    }

    /**
     * execute the shell command in specified path,if the path is null,then executing directory is default
     *
     * @param command shell command
     * @return ShellResult which contains stderr and stdout
     **/
    public static ShellResult executeCommand(String command, String path, Optional<String> charset) {
        try {
            // generate command line according to platform
            String[] commander = platformSpecificCommand(command);
            Process process;
            if (path != null) {
                process = Runtime.getRuntime().exec(commander, null, new File(path));
            } else {
                process = Runtime.getRuntime().exec(commander);
            }

            // use a separate process to read buffer to avoid os buffer overflow
            List<CompletableFuture<ShellResult>> resultFuture = Stream.of(process)
                    .map(cmdlineProcess -> CompletableFuture.supplyAsync(() -> {
                        ShellResult result = new ShellResult();
                        ByteConvenientStream stdout = new ByteConvenientStream();
                        ByteConvenientStream stderr = new ByteConvenientStream();
                        InputStream inputStream = cmdlineProcess.getInputStream();
                        InputStream errStream = cmdlineProcess.getErrorStream();
                        try {
                            while (inputStream.available() > 0 || errStream.available() > 0 || process.isAlive()) {
                                stdout.writeWithInputStream(inputStream);
                                stderr.writeWithInputStream(errStream);
                            }
                            inputStream.close();
                            errStream.close();

                            result.stdout = new String(stdout.toByteArray(), charset.orElse(StandardCharsets.UTF_8.name()));
                            result.stderr = new String(stderr.toByteArray(), charset.orElse(StandardCharsets.UTF_8.name()));
                            if (process.exitValue() != 0) {
                                result.isSuccess = false;
                            } else {
                                result.isSuccess = true;
                            }
                            cmdlineProcess.destroy();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return result;
                    })).collect(Collectors.toList());
            process.waitFor(Common.COMMAND_LINE_TIMEOUT, TimeUnit.SECONDS);
            if (!resultFuture.isEmpty()) {
                return resultFuture.get(0).get();
            } else {
                throw new RuntimeException("process exit but result not found");
            }
        } catch (Exception e) {
            ShellResult shellResult = new ShellResult();
            shellResult.isSuccess = false;
            shellResult.stderr = Arrays.toString(e.getStackTrace());
            shellResult.stdout = "failed";
            return shellResult;
        }
    }

    /**
     * interactive exec a shell command on remote machine
     *
     * @param channel   channel opened by SSHManager session
     * @param command   shell command in bytes(due to some nonprintables key press)
     * @param sendEnter issue an enter key press after this command exec,do it if true
     * @throws Exception any reason failed
     **/
    public static String interactiveExecRemoteCommand(Channel channel, byte[] command, boolean sendEnter) throws Exception {


        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        channel.setOutputStream(responseStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(command);
        channel.setInputStream(inputStream);
        PrintStream printStream = new PrintStream(responseStream);
        Expect expect = new ExpectBuilder()
                .withOutput(channel.getOutputStream())
                .withInputs(channel.getInputStream(), channel.getExtInputStream())
                //.withEchoOutput(System.out)
                .withEchoInput(printStream)
                .withInputFilters(removeColors(), removeNonPrintable())
                .withExceptionOnFailure()
                .build();
        expect.sendBytes(command);
        if (sendEnter) expect.sendLine();
        responseStream.flush();
        Thread.sleep(1000);
        byte[] rep = responseStream.toByteArray();

        // clear resource
        responseStream.close();
        inputStream.close();
        expect.close();

        return new String(rep);
    }

    public static ShellResult executeCommand(String command) throws Exception {
        return executeCommand(command, null, Optional.empty());
    }

    /**
     * request new SSH connection terminal,please reuse it as often as usable,
     * you can issue many command with SSHManager
     *
     * @param username username,such as 'root'
     * @param password user password
     * @param ip       remote machine ip
     * @return SSHManager represent a window in xshell
     **/
    public static SSHManager requestNewSSHConnection(String username, String password, String ip) throws Exception {
        SSHManager instance = new SSHManager(new SSHConnectionInfo(username, password, ip));
        instance.connect();
        return instance;
    }


    // generate platform specific command line
    private static String[] platformSpecificCommand(String command) {
        switch (getCurrentOSType()) {
            case WINDOWS:
                return new String[]{"cmd", "/c", command};
            default:
                return new String[]{"/bin/bash", "-c", command};
        }
    }


    /**
     * get current {@link OSType}
     *
     * @return OSType
     **/
    public static OSType getCurrentOSType() {
        String s = System.getProperty("os.name").toLowerCase();
        if (s.contains("linux")) return OSType.LINUX;
        if (s.contains("windows")) return OSType.WINDOWS;
        return OSType.OTHERS;
    }

    /**
     * get platform-specific path separator
     *
     * @return path separator
     **/
    public static String pathSeparator() {
        switch (getCurrentOSType()) {
            case WINDOWS:
                return "\\";
            default:
                return "/";
        }
    }

    /**
     * get platform-specific command line separator which separate multi-command
     *
     * @return path separator
     **/
    public static String commandSeparator() {
        switch (getCurrentOSType()) {
            case WINDOWS:
                return "&";
            default:
                return ";";
        }
    }

}

