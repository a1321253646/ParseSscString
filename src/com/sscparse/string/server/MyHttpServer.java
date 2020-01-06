package com.sscparse.string.server;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.sscparse.string.utils.TextUtils;
import com.sun.net.httpserver.HttpExchange;  
import com.sun.net.httpserver.HttpHandler;  
import com.sun.net.httpserver.HttpServer;  
import com.sun.net.httpserver.spi.HttpServerProvider;

import com.sscparse.string.RegularUtils2;

public class MyHttpServer {  
    //�������񣬼������Կͻ��˵�����  
    public static void httpserverService() throws IOException {  
        HttpServerProvider provider = HttpServerProvider.provider();  
        HttpServer httpserver =provider.createHttpServer(new InetSocketAddress(8803), 100);//�����˿�6666,��ͬʱ�� ��100������  
        httpserver.createContext("/ssc", new MyHttpHandler());   
        httpserver.setExecutor(null);  
        httpserver.start();       
        System.out.println("server started");  
    }  
    //Http��������  
    static class MyHttpHandler implements HttpHandler {  
        public void handle(HttpExchange httpExchange) throws IOException {  
            String responseMsg = "ok";   //��Ӧ��Ϣ  
            //InputStream in = httpExchange.getRequestBody(); //��������� 
            String queryString =  getParm(httpExchange.getRequestBody());  
            System.out.println("client handle:\n"+queryString);  
            
            //String queryString =  httpExchange.getRequestURI().getQuery();        
            
            //Map<String,String> queryStringInfo = formData2Dic(queryString);
            Map<String,String> queryStringInfo = jsonToMap(queryString);
            String ms = null;
            String orderid = null;
            
            if(queryStringInfo.containsKey("ms")) {
            	ms =  queryStringInfo.get("ms");            
            }
            if(queryStringInfo.containsKey("orderid")) {
            	orderid =  queryStringInfo.get("orderid");            
            }
            if(!TextUtils.isEmpty(ms) ){
            	String json = RegularUtils2.regularStr(ms,orderid);
            	if(!TextUtils.isEmpty(json)) {
            		responseMsg = json;
            	}else {
            		responseMsg = "error";
            	}               	
            }else {           
            	responseMsg = "error";
            }
            System.out.println("client handle: return responseMsg="+responseMsg);
            
            httpExchange.sendResponseHeaders(200, responseMsg.length()); //������Ӧͷ���Լ���Ӧ��Ϣ�ĳ���  
            OutputStream out = httpExchange.getResponseBody();  //��������  
            out.write(responseMsg.getBytes());  
            out.flush();  
            httpExchange.close();                                            
        }  
    }  
    
    public static  Map<String,String> jsonToMap(String str){
    	Map<String,String> result = new HashMap<>();
    	
    	try {
    		JSONObject jb = new JSONObject(str);
        	if(jb.has("ms")) {
        		result.put("ms", jb.getString("ms"));
        	}
        	if(jb.has("orderid")) {
        		result.put("orderid",jb.getInt("orderid")+"");
        	}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

    	return result;
    }
    
    
    public static String getParm(InputStream request) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(request, "UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String line = null;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }
    
    public static Map<String,String> formData2Dic(String formData ) {
        Map<String,String> result = new HashMap<>();
        if(formData== null || formData.trim().length() == 0) {
            return result;
        }
        final String[] items = formData.split("&");
        for(String item: items) {
            String[] keyAndVal = item.split("=");
            if( keyAndVal.length == 2) {
                try{
                    final String key = URLDecoder.decode( keyAndVal[0],"utf8");
                    final String val = URLDecoder.decode( keyAndVal[1],"utf8");
                    result.put(key,val);
                }catch (UnsupportedEncodingException e) {}
            }
        }
        return result;
    }
    
    public static void main(String[] args) throws IOException {  
        httpserverService();  
    }  
}  