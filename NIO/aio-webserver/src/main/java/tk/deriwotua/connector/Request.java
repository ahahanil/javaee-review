package tk.deriwotua.connector;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * HTTP协议格式
 *      GET /index.html HTTP/1.1
 *      Host: localhost:8888
 *      Connection: keep-alive
 *      Cache-Control: max-age=0
 *      Upgrade-Insecure-Requests: 1
 *      User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36
 */
public class Request implements ServletRequest {

    private static final int BUFFER_SIZE = 1024;

    /**
     * 输入流，即和socket所对应的InputStream
     */
    private InputStream input;

    /**
     * 请求的uri，如 /index.html
     */
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    public String getRequestURI() {
        return uri;
    }

    /**
     * 解析HTTP协议
     */
    public void parse() {
        int length = 0;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            length = input.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 获取请求的所有信息
        StringBuilder request = new StringBuilder();
        for (int j = 0; j < length; j++) {
            request.append((char) buffer[j]);
        }

        // 解析uri
        uri = parseUri(request.toString());
    }

    /**
     * 解析请求中的请求url
     * <p>
     * 假设请求是有空格分割的内容，要获取的就是第一个空格与第二个空格之间的内容
     *
     * 假设请求的格式如下：
     *         GET /index.html HTTP/1.1
     *         Host: localhost:8888
     *         Connection: keep-alive
     *         Cache-Control: max-age=0
     *         Upgrade-Insecure-Requests: 1
     *         User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36
     *
     * @param s
     * @return
     */
    private String parseUri(String s) {
        int index1, index2;

        // 获取第一个空格的位置
        index1 = s.indexOf(' ');
        if (index1 != -1) {
            // 从第一个空格后边的位置开始寻找第二个空格
            index2 = s.indexOf(' ', index1 + 1);

            // 获取url
            if (index2 > index1) {
                return s.substring(index1 + 1, index2);
            }
        }
        return "";
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}