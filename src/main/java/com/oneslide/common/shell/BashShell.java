package com.oneslide.common.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

                            result.stdout = new String(stdout.toByteArray(),charset.orElse(StandardCharsets.UTF_8.name()));
                            result.stderr = new String(stderr.toByteArray(),charset.orElse(StandardCharsets.UTF_8.name()));
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

    public static ShellResult executeCommand(String command) throws Exception {
        return executeCommand(command, null,Optional.empty());
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

