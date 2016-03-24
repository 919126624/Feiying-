package com.wuyizhiye.framework.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 自定义一个响应结果包装器，将在这里提供一个基于内存的输出器来存储所有
 * 返回给客户端的data
 * 
 *
 */
public class Wrapper  extends HttpServletResponseWrapper {
    public static final int OT_NONE = 0, OT_WRITER = 1, OT_STREAM = 2;
    private int outputType = OT_NONE;
    private ServletOutputStream output = null;
	// 这个是包装PrintWriter的，可以在写入同时对写入的数据进行格式化。
    private PrintWriter writer = null;
    private ByteArrayOutputStream buffer = null;

    public Wrapper(HttpServletResponse resp) throws IOException {
        super(resp);
        //可以捕获内存缓冲区的数据，转换成字节数组。用于读取
        buffer = new ByteArrayOutputStream();
    }

    //重新response.getWriter()的数据
	@Override
    public PrintWriter getWriter() throws IOException {
        if (outputType == OT_STREAM)
            throw new IllegalStateException();
        else if (outputType == OT_WRITER)
            return writer;
        else {
            outputType = OT_WRITER;

    		// 这个是包装PrintWriter的，让所有结果通过这个PrintWriter写入到bufferedWriter中。用于读取
            writer = new PrintWriter(new OutputStreamWriter(buffer,
                    getCharacterEncoding()));
            return writer;
        }
    }

    public ServletOutputStream getOutputStream() throws IOException {//用于读取，WrappedOutputStream重写的writer的方法
        if (outputType == OT_WRITER)
            throw new IllegalStateException();
        else if (outputType == OT_STREAM)
            return output;
        else {
            outputType = OT_STREAM;
            output = new WrappedOutputStream(buffer);
            return output;
        }
    }

	@Override
    public void flushBuffer() throws IOException {//对stream进行flush
        if (outputType == OT_WRITER)
            writer.flush();
        if (outputType == OT_STREAM)
            output.flush();
    }

    public void reset() {
        outputType = OT_NONE;
        buffer.reset();
    }

    public byte[] getResponseData() throws IOException {//转成byte
        flushBuffer();
        return buffer.toByteArray();

    }

    class WrappedOutputStream extends ServletOutputStream {//是一个内部class
        private ByteArrayOutputStream buffer;

        public WrappedOutputStream(ByteArrayOutputStream buffer) {
            this.buffer = buffer;
        }

    	@Override
        public void write(int b) throws IOException {
            buffer.write(b);
        }

        public byte[] toByteArray() {
            return buffer.toByteArray();
        }
    }

}
