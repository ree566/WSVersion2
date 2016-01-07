/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wst.cls;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Wei.Cheng
 */
public class HttpAsyncClientDemo {

    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws Exception {

        HttpAsyncClientDemo http = new HttpAsyncClientDemo();

//        System.out.println("Testing 1 - Send Http GET request");
//        http.sendGet();
        System.out.println("\nTesting 2 - Send Http POST request");
        http.sendPost();

    }

    // HTTP GET request
    private void sendGet() throws Exception {

        String url = "http://www.google.com/search?q=developer";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://mestest.azurewebsites.net/EFFICIENCY_REPORT/Wip_Info_Active.aspx?PROVIDER_ID=1";
        try {
            AsyncHttpClient ahc = new AsyncHttpClient();
            Future<Response> f
                    = ahc.preparePost(url + "/offset")
                    .addParameter("cboWerks", "ATMU-System")
                    .addParameter("cboUnit", "")
                    .addParameter("txtItemNo", "")
                    .addParameter("txtDeptday", "10")
                    .execute();
            System.out.println("Response Code : " + f.get());
        } catch (IOException lException) {
            throw new RuntimeException("IO Problems executing POST, underlying problem is " + lException.getMessage(), lException);
        }
    }
}
