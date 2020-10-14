package com.ratel.sign.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by yxc on 2020/10/12.
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        String sessionStream = this.getBodyString(request);
        this.body = sessionStream.getBytes(Charset.forName("UTF-8"));
    }

    public String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();

        try {
            InputStream inputStream = this.cloneInputStream(request.getInputStream());
            Throwable var4 = null;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                Throwable var6 = null;

                try {
                    String line;
                    try {
                        while((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    } catch (Throwable var31) {
                        var6 = var31;
                        throw var31;
                    }
                } finally {
                    if(reader != null) {
                        if(var6 != null) {
                            try {
                                reader.close();
                            } catch (Throwable var30) {
                                var6.addSuppressed(var30);
                            }
                        } else {
                            reader.close();
                        }
                    }

                }
            } catch (Throwable var33) {
                var4 = var33;
                throw var33;
            } finally {
                if(inputStream != null) {
                    if(var4 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var29) {
                            var4.addSuppressed(var29);
                        }
                    } else {
                        inputStream.close();
                    }
                }

            }
        } catch (IOException var35) {
            var35.printStackTrace();
        }

        return sb.toString();
    }

    public InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        try {
            int len;
            while((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }

            byteArrayOutputStream.flush();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(this.body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}