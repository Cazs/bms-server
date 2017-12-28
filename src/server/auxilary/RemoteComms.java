/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.auxilary;

/*import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fadulousbms.exceptions.LoginException;
import fadulousbms.managers.SessionManager;
import fadulousbms.model.BusinessObject;
import fadulousbms.model.Error;*/
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.controllers.CounterController;
import server.exceptions.InvalidBusinessObjectException;
import server.exceptions.InvalidJobException;
import server.model.BusinessObject;
import server.model.Counter;
import sun.net.www.http.HttpClient;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.rmi.Remote;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ghost
 */
public class RemoteComms
{
    public static String host = "http://192.168.43.31:9000";//192.168.0.103//95.85.57.110
    public static final String TAG = "RemoteComms";
    public static String DB_IP = "localhost";
    public static int DB_PORT = 27017;
    public static String DB_NAME = "fadulousbms";
    public static int TTL = 60*60*2;//2 hours in sec

    public static void setHost(String h)
    {
        host = h;
    }

    public static boolean pingServer() throws IOException
    {
        URL urlConn = new URL(host);
        HttpURLConnection httpConn =  (HttpURLConnection)urlConn.openConnection();

        boolean response = (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK);
        httpConn.disconnect();
        return response;
    }

    public static String sendGetRequest(String url, ArrayList<AbstractMap.SimpleEntry<String,String>> headers) throws IOException
    {
        IO.log(TAG, IO.TAG_INFO, String.format("\nGET %s HTTP/1.1\nHost: %s", url, host));

        URL urlConn = new URL(host + url);
        HttpURLConnection httpConn =  (HttpURLConnection)urlConn.openConnection();
        for(AbstractMap.SimpleEntry<String,String> header:headers)
            httpConn.setRequestProperty(header.getKey() , header.getValue());
        
        String response = null;
        if(httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            response="";
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line="";
            int read=0;
            while ((line=in.readLine())!=null)
                response += line;
            //Log.d(TAG,response);
        }else
        {
            response="";
            BufferedReader in = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
            String line="";
            int read=0;
            while ((line=in.readLine())!=null)
                response += line;
            IO.logAndAlert("GET Error", response, IO.TAG_ERROR);
        }

        IO.log(TAG, IO.TAG_INFO, "GET response> " + response + "\n");
        return response;
    }

    public static byte[] sendFileRequest(String file_url, ArrayList<AbstractMap.SimpleEntry<String,String>> headers) throws IOException
    {
        IO.log(TAG, IO.TAG_INFO, String.format("\nGET %s HTTP/1.1", file_url));

        URL urlConn = new URL(host + file_url);
        //URL urlConn = new URL("http://127.0.0.1:9000/api/file/inspection/3-demolition.pdf");
        try(InputStream in = urlConn.openStream())
        {
            //Files.copy(in, new File("download.pdf").toPath(), StandardCopyOption.REPLACE_EXISTING);
            //DataInputStream dataInputStream = new DataInputStream(in);


            ByteArrayOutputStream outbytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read=0;
            while ((read=in.read(buffer, 0, buffer.length))>0)
                outbytes.write(buffer, 0, read);
            outbytes.flush();
            in.close();
            IO.log(TAG, IO.TAG_INFO, "GET received file> " + file_url + " " + outbytes.toByteArray().length + " bytes.\n");
            return outbytes.toByteArray();
        }
        //URL urlConn = new URL(host);
        /*HttpURLConnection httpConn =  (HttpURLConnection)urlConn.openConnection();

        for(AbstractMap.SimpleEntry<String,String> header:headers)
            httpConn.setRequestProperty(header.getKey() , header.getValue());


        String response = null;
        if(httpConn.getResponseCode() == HttpURLConnection.HTTP_OK)
        {
            response="";
            DataInputStream in = new DataInputStream(httpConn.getInputStream());

            ByteArrayOutputStream outbytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read=0;
            while ((read=in.read(buffer, 0, buffer.length))>0)
            {
                outbytes.write(buffer, 0, read);
            }
            outbytes.flush();
            in.close();
            IO.log(TAG, IO.TAG_INFO, "GET received file> " + filename + " " + outbytes.toByteArray().length + "bytes.\n");
            return outbytes.toByteArray();
        }else
        {
            IO.log(TAG, IO.TAG_ERROR, IO.readStream(httpConn.getErrorStream()));
            return null;
        }*/
    }
    
    public static HttpURLConnection postData(String function, ArrayList<AbstractMap.SimpleEntry<String,String>> params, ArrayList<AbstractMap.SimpleEntry<String,String>> headers) throws IOException
    {
        URL urlConn = new URL(host + function);
        HttpURLConnection httpConn = (HttpURLConnection)urlConn.openConnection();
        if(headers!=null)
            for(AbstractMap.SimpleEntry<String,String> header:headers)
                httpConn.setRequestProperty(header.getKey() , header.getValue());
        httpConn.setReadTimeout(10000);
        httpConn.setConnectTimeout(15000);
        httpConn.setRequestMethod("POST");
        httpConn.setDoInput(true);
        httpConn.setDoOutput(true);

        //Encode body data in UTF-8 charset
        StringBuilder result = new StringBuilder();
        for(int i=0;i<params.size();i++)
        {
            AbstractMap.SimpleEntry<String,String> entry = params.get(i);
            if(entry!=null)
            {
                if(entry.getKey()!=null && entry.getValue()!=null)
                {
                    result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                    result.append((i != params.size() - 1 ? "&" : ""));
                }else return null;
            }else return null;
        }

        IO.log(TAG, IO.TAG_INFO, String.format("POST %s HTTP/1.1\nHost: %s", function, host));

        //Write to server
        OutputStream os = httpConn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
        writer.write(result.toString());
        writer.flush();
        writer.close();
        os.close();

        //httpConn.connect();
        
        /*Scanner scn = new Scanner(new InputStreamReader(httpConn.getErrorStream()));
        String resp = "";
        while(scn.hasNext())
            resp+=scn.nextLine();
        System.err.println(resp);*
        String resp = httpConn.getHeaderField("Set-Cookie");
        System.err.println(resp);*/
        
        return httpConn;
    }

    public static HttpURLConnection postData(String function, String object, ArrayList<AbstractMap.SimpleEntry<String,String>> headers) throws IOException
    {
        URL urlConn = new URL(host + function);
        HttpURLConnection httpConn = (HttpURLConnection)urlConn.openConnection();
        if(headers!=null)
            for(AbstractMap.SimpleEntry<String,String> header:headers)
                httpConn.setRequestProperty(header.getKey() , header.getValue());
        httpConn.setReadTimeout(10000);
        httpConn.setConnectTimeout(15000);
        httpConn.setRequestMethod("POST");
        httpConn.setDoInput(true);
        httpConn.setDoOutput(true);

        IO.log(TAG, IO.TAG_INFO, String.format("POST %s HTTP/1.1\nHost: %s", function, host));

        //Write to server
        OutputStream os = httpConn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
        writer.write(object);
        writer.flush();
        writer.close();
        os.close();

        return httpConn;
    }

    public static void uploadFile(String endpoint, ArrayList<AbstractMap.SimpleEntry<String,String>> headers, byte[] file) throws IOException
    {
        URL urlConn = new URL(host + endpoint);
        HttpURLConnection httpConn = (HttpURLConnection)urlConn.openConnection();
        if(headers!=null)
            for(AbstractMap.SimpleEntry<String,String> header:headers)
                httpConn.setRequestProperty(header.getKey() , header.getValue());

        httpConn.setRequestProperty("Content-Length", String.valueOf(file.length));
        httpConn.setReadTimeout(10000);
        httpConn.setConnectTimeout(15000);
        httpConn.setRequestMethod("POST");
        httpConn.setDoInput(true);
        httpConn.setDoOutput(true);

        IO.log(TAG, IO.TAG_INFO, String.format("POST %s HTTP/1.1\nHost: %s", endpoint, host));

        //Write to server
        OutputStream os = httpConn.getOutputStream();
        //OutputStreamWriter writer = new OutputStreamWriter(os);
        os.write(file);
        os.flush();
        os.close();

        httpConn.connect();
        String desc = IO.readStream(httpConn.getInputStream());
        IO.log(RemoteComms.class.getName(), httpConn.getResponseCode() + ":\t" + desc, IO.TAG_INFO);
        httpConn.disconnect();
    }

    public static String commitBusinessObjectToDatabase(BusinessObject businessObject, String collection, String timestamp_name)
    {
        if(businessObject!=null)
        {
            long date_logged = System.currentTimeMillis();
            businessObject.setDate_logged(date_logged);
            if (businessObject.isValid())
            {
                IO.log(RemoteComms.class.getName(),IO.TAG_INFO, "creating new BusinessObject{"+businessObject.getClass().getName()+"}: "+businessObject.toString());
                //commit BusinessObject data to DB server
                if(collection!=null)
                    IO.getInstance().mongoOperations().insert(businessObject, collection);
                else return null;
                IO.log(RemoteComms.class.getName(),IO.TAG_INFO, "committed business object: ["+businessObject.get_id()+"]");
                //update respective timestamp
                if(timestamp_name!=null)
                    CounterController.updateCounter(new Counter(timestamp_name, date_logged));
                else return null;
                return businessObject.get_id();
            } else throw new InvalidBusinessObjectException();
        }
        IO.log(Remote.class.getName(),IO.TAG_ERROR, "invalid Business Object.");
        return null;
    }
}
