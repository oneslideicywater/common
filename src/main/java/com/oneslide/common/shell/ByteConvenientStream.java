package com.oneslide.common.shell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteConvenientStream extends ByteArrayOutputStream {

    private byte[] buffer=new byte[1024];

    /**
     * write to ByteArrayStream from input stream
     *
     **/
    public void writeWithInputStream(InputStream inputStream) throws IOException {
        int n;
        while ((n = inputStream.read(buffer)) != -1) {
            write(buffer, 0, n);
        }
    }
}
