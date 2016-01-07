/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wst.cls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Wei.Cheng
 */
public class HTTPBaseIO {

    public enum Method {

        get, post
    }

    private DefaultHttpClient httpclient = null;
    private boolean isClosedConn = false;
    private String newuri = null;
    private int statuscode = HttpStatus.SC_NO_CONTENT;

    private HttpHost proxy = null;

    public HTTPBaseIO() {

    }

    public HTTPBaseIO(String proxyIP, int proxyPort) {
        setProxy(proxyIP, proxyPort);
    }

    public HTTPBaseIO(HttpHost proxy) {
        setProxy(proxy);
    }

    /**
     * 取得使用的proxy
     *
     * @return
     */
    public HttpHost getProxy() {
        return proxy;
    }

    /**
     * 設定proxy
     *
     * @param proxy
     */
    private void setProxy(HttpHost proxy) {
        this.proxy = proxy;
    }

    /**
     * 設定proxy
     *
     * @param ip proxy的IP(hostname)
     * @param port proxy的Port
     */
    private void setProxy(String ip, int port) {
        if (ip != null) {
            proxy = new HttpHost(ip, port);
        }
    }

    /**
     * 取得回應後所得到的代碼，可參考org.apache.http.HttpStatus類別
     *
     * @return org.apache.http.HttpStatus
     */
    public int getStatuscode() {
        return statuscode;
    }

    /**
     * 如果是轉導的狀態所得到的URI
     *
     * @return
     */
    public String getNewuri() {
        return newuri;
    }

    public void resetNewuri() {
        newuri = null;
    }

    /**
     * 取得連線物件
     *
     * @return
     */
    public DefaultHttpClient getHttpclient() {
        return httpclient;
    }

    /**
     * 設定連線物件
     *
     * @param httpclient
     */
    public void setHttpclient(DefaultHttpClient httpclient) {
        this.httpclient = httpclient;
    }

    /**
     * 是否已關閉連線
     *
     * @return
     */
    public boolean isClosedConn() {
        return isClosedConn;
    }

    /**
     * 關閉連線
     */
    public void closeConn() {
        closeConn(true);
    }

    /**
     * 關閉連線
     *
     * @param isCloseConn 是否關閉
     */
    public void closeConn(boolean isCloseConn) {
        if (isCloseConn && httpclient != null && !isClosedConn) {
            httpclient.getConnectionManager().shutdown();
            httpclient = null;
            isClosedConn = true;
        }
    }

    /**
     * 取得網頁內容
     *
     * @param urlpath 網址
     * @param method Method.get or Method.post
     * @param params 參數
     * @param charset 編碼，如HTTP.UTF_8
     * @param isAutoRedirect
     * 如果網頁回應狀態為轉導到新網頁，且Header的location有值，則自己以location所指網址取得內容
     * @param isCloseConn 是否關閉連線
     * @return 失敗回傳null，成功回傳網頁HTML
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doSend(String urlpath, Method method, String params,
            String charset, boolean isAutoRedirect, boolean isCloseConn)
            throws ClientProtocolException, IOException {
        return doSendBase(urlpath, method, StringToHttpEntity(params, charset),
                charset, isAutoRedirect, isCloseConn);
    }

    /**
     * 取得網頁內容
     *
     * @param urlpath 網址
     * @param method Method.get or Method.post
     * @param params 參數
     * @param charset 編碼，如HTTP.UTF_8
     * @param isAutoRedirect
     * 如果網頁回應狀態為轉導到新網頁，且Header的location有值，則自己以location所指網址取得內容
     * @param isCloseConn 是否關閉連線
     * @return 失敗回傳null，成功回傳網頁HTML
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doSend(String urlpath, Method method,
            List params, String charset, boolean isAutoRedirect,
            boolean isCloseConn) throws ClientProtocolException, IOException {
        return doSendBase(urlpath, method, ListToHttpEntity(params, charset),
                charset, isAutoRedirect, isCloseConn);
    }

    /**
     * 取得網頁內容
     *
     * @param urlpath 網址
     * @param method Method.get or Method.post
     * @param params 參數
     * @param charset 編碼，如HTTP.UTF_8
     * @param isAutoRedirect
     * 如果網頁回應狀態為轉導到新網頁，且Header的location有值，則自己以location所指網址取得內容
     * @param isCloseConn 是否關閉連線
     * @return 失敗回傳null，成功回傳網頁HTML
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doSend(String urlpath, Method method,
            HashMap params, String charset,
            boolean isAutoRedirect, boolean isCloseConn)
            throws ClientProtocolException, IOException {
        return doSendBase(urlpath, method,
                HashMapToHttpEntity(params, charset), charset, isAutoRedirect,
                isCloseConn);
    }

    /**
     * 取得網頁內容
     *
     * @param urlpath 網址
     * @param method Method.get or Method.post
     * @param params 參數
     * @param charset 編碼，如HTTP.UTF_8
     * @param isAutoRedirect
     * 如果網頁回應狀態為轉導到新網頁，且Header的location有值，則自己以location所指網址取得內容
     * @param isCloseConn 是否關閉連線
     * @return 失敗回傳null，成功回傳網頁HTML
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String doSendBase(String urlpath, Method method, HttpEntity params,
            String charset, boolean isAutoRedirect, boolean isCloseConn)
            throws ClientProtocolException, IOException {
        String responseBody = null;
        HttpUriRequest httpgetpost = null;

        statuscode = HttpStatus.SC_NO_CONTENT;
        try {
            if (httpclient == null || isClosedConn()) {
                httpclient = new DefaultHttpClient();
            }

            if (proxy != null) {
                httpclient.getParams().setParameter(
                        ConnRoutePNames.DEFAULT_PROXY, proxy);
            }

            if (method == Method.post) {
                httpgetpost = new HttpPost(urlpath);
                if (params != null) {
                    ((HttpPost) httpgetpost).setEntity(params);
                }
            } else {
                if (params != null) {
                    urlpath += "?"
                            + inputStream2String(params.getContent(), charset);
                }
                httpgetpost = new HttpGet(urlpath);
            }

            HttpResponse response = httpclient.execute(httpgetpost);
            statuscode = response.getStatusLine().getStatusCode();
            if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY)
                    || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
                    || (statuscode == HttpStatus.SC_SEE_OTHER)
                    || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                Header header = response.getFirstHeader("location");

                if (header != null) {
                    newuri = header.getValue();
                    if ((newuri == null) || (newuri.equals(""))) {
                        newuri = "/";
                    }
                    if (isAutoRedirect) {
                        httpgetpost.abort();
                        httpgetpost = null;
                        responseBody = doSendBase(newuri, Method.get, null,
                                charset, true, false);
                    }
                }
            } else if (statuscode == HttpStatus.SC_OK) {
                responseBody = inputStream2String(response.getEntity()
                        .getContent(), charset);
            }
        } catch (ClientProtocolException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } finally {
            if (httpgetpost != null) {
                httpgetpost.abort();
                httpgetpost = null;
            }
            closeConn(isCloseConn);
        }
        return responseBody;
    }

    /**
     * 將inputStream轉為String
     *
     * @param is inputStream
     * @param charset 編碼，如HTTP.UTF_8
     * @return inputStream的內容
     * @throws UnsupportedEncodingException
     */
    public static String inputStream2String(InputStream is, String charset)
            throws UnsupportedEncodingException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is,
                charset));
        StringBuilder buffer = new StringBuilder();
        String line = "";
        try {
            boolean isfirst = true;
            while ((line = in.readLine()) != null) {
                if (!isfirst) {
                    buffer.append("\n");
                } else {
                    isfirst = false;
                }
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    /**
     * List轉為HttpEntity
     *
     * @param nvps
     * @param charset 編碼，如HTTP.UTF_8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static HttpEntity ListToHttpEntity(List nvps,
            String charset) throws UnsupportedEncodingException {
        HttpEntity result = null;
        if (nvps != null && nvps.size() > 0) {
            result = new UrlEncodedFormEntity(nvps, charset);

        }
        return result;
    }

    /**
     * String to HttpEntity(
     *
     * @param nvps
     * @param charset 編碼，如HTTP.UTF_8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static HttpEntity StringToHttpEntity(String nvps, String charset)
            throws UnsupportedEncodingException {
        HttpEntity result = null;
        if (nvps != null) {
            StringEntity reqEntity = new StringEntity(nvps, charset);
            reqEntity.setContentType("application/x-www-form-urlencoded");
            result = reqEntity;
        }
        return result;
    }

    /**
     * HashMap To HttpEntity
     *
     * @param nvps
     * @param charset 編碼，如HTTP.UTF_8
     * @return
     * @throws UnsupportedEncodingException
     */
    public static HttpEntity HashMapToHttpEntity(HashMap nvps,
            String charset) throws UnsupportedEncodingException {
        HttpEntity result = null;
        if (nvps != null) {
            result = new StringEntity(hashMapToString(nvps, charset), charset);
            try {
                result = StringToHttpEntity(inputStream2String(result
                        .getContent(), charset), charset);
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * hashMapToString
     *
     * @param map
     * @param charset
     * @param charset編碼 ，如HTTP.UTF_8
     * @return
     * @throws UnsupportedEncodingException
     */
    @SuppressWarnings("unchecked")
    public static String hashMapToString(HashMap map,
            String charset) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        java.util.Iterator it = map.entrySet().iterator();
        boolean isfirst = true;
        while (it.hasNext()) {
            java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
            if (isfirst) {
                isfirst = false;
            } else {
                result.append("&");
            }
            result
                    .append(URLEncoder.encode(entry.getKey().toString(),
                                    charset));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue().toString(),
                    charset));
        }
        return result.toString();
    }

    public static ArrayList paramToArray(String param)
            throws UnsupportedEncodingException {
        ArrayList arr = null;
        if (param != null) {
            String[] p = param.split("&");
            if (param.toLowerCase().contains("&")) {
                ArrayList p2 = new ArrayList();
                int j = 0;
                for (String p1 : p) {
                    if (p1.toLowerCase().startsWith("amp;")) {
                        p2.set(j - 1, p2.get(j - 1) + "&" + p1.substring(4));
                        j--;
                    }
                    p2.add(p1);
                    j++;
                }
                p2.toArray(p);
            }

            for (String p1 : p) {
                String[] item = p1.split("=");
                if (item.length == 2) {
                    if (arr == null) {
                        arr = new ArrayList();
                    }
                    // item[0]=URLDecoder.decode(item[0],charset);
                    // item[1]=URLDecoder.decode(item[1],charset);  
                    arr.add(item);
                }
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        String urltest = "http://172.20.131.52/bandon/login.php";
        String charset = "UTF-8";
        HTTPBaseIO.Method method = HTTPBaseIO.Method.get;
        HTTPBaseIO reqClient = new HTTPBaseIO();
        try {
            String html = reqClient.doSendBase(urltest, method, null, charset, false, false);
            System.out.println(html);
            if (html == null) {
                System.out.println("Client get nothing");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reqClient.closeConn();
        }
        
    }
}
