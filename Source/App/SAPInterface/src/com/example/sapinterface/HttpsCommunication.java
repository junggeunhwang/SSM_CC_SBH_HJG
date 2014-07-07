package com.example.sapinterface;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyStore;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpsCommunication 
{
	public static final String TAG = "HttpsCommunication";
	public static final String TYPE_STRING = "string";
	public static final String TYPE_FILE = "file";
	public static final String TYPE_REQUEST = "request";
	private final String urlString = "https://221.146.188.179";
	
	private boolean isUniqueNumberSet = false;
	private boolean isTypeSet = false;
	private boolean isDataSet = false;
	private HttpClient httpClient;
	private MultipartEntityBuilder builder;
	private Thread requestThread;
	private HttpResponse response;
	private HttpsCommunicationCallback callback;
	private HttpsCommunication thispointer;
	
	//response
	private String responseType;
	private String responseUniqueNumber;
	private String responseOrder;
	private byte[] responseByteData;
	private String responseStringData;
	
	// default constructor
	
	
	public HttpsCommunication(HttpsCommunicationCallback cb)
	{
		httpClient = getHttpClient();
		builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		callback = cb;
		thispointer = this;
		requestThread = new Thread(){
			@Override
            public void run() {
                
            	try 
                {
                    URI url = new URI(urlString);

                    HttpPost httpPost = new HttpPost();
                    httpPost.setURI(url);
                    httpPost.setEntity(builder.build());
              
                    response = httpClient.execute(httpPost);
                  
                    responseType = response.getFirstHeader("type").getValue();
                    responseUniqueNumber = response.getFirstHeader("src").getValue();
                    responseOrder = response.getFirstHeader("order").getValue();
                  
                    Log.d(TAG,"type : " +responseType);
                    Log.d(TAG,"src number : "+responseUniqueNumber);
                    
                    HttpEntity a = response.getEntity();
                                                                                
                    
           		 	responseByteData = EntityUtils.toByteArray(a);
           		 	
           		 	responseStringData = new String(responseByteData,"UTF-8");
           
                    callback.onResponseSuccess(thispointer);

                } catch (URISyntaxException e) 
                {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                    callback.onResponseFailure(e.getLocalizedMessage());
                } catch (ClientProtocolException e) 
                {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                    callback.onResponseFailure(e.getLocalizedMessage());
                } catch (IOException e) 
                {
                    Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                    callback.onResponseFailure(e.getLocalizedMessage());
                } catch (Exception e)
                {
                	Log.e(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                    callback.onResponseFailure(e.getLocalizedMessage());
                }

            }
		};
	}
	
	// method
	/**
	 * getHttpClient 
	 * @return HttpClient Object
	 */
	private HttpClient getHttpClient() 
    {
        try 
        {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SFSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), SFSSLSocketFactory.HTTP_PORT));
            registry.register(new Scheme("https", sf, SFSSLSocketFactory.HTTPS_PORT));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
	/**
	 * setUniqueNumber
	 * @param uniqueNumber - phone number(String)
	 */
	public void setUniqueNumber(String uniqueNumber)
	{
		if(uniqueNumber != null)
		{
			builder.addTextBody("uniqueNumber", uniqueNumber);
			isUniqueNumberSet = true;
		}
	}
	
	public void setType(String type)
	{
		if(type != null)
		{
			builder.addTextBody("type", type);
			isTypeSet = true;
		}
	}
	
	public void setStringData(String data)
	{
		if(data != null)
		{
			builder.addTextBody("data", data);
			isDataSet = true;
		}
	}
	
	public void setFileData(File file)
	{
		if(file != null)
		{
			builder.addBinaryBody("data", file);
			isDataSet = true;
		}
	}
	public void setExtraData(String extradata)
	{
		if(extradata != null)
		{
			builder.addTextBody("extradata", extradata);
		}
	}	
	public boolean ExecuteRequest()
	{
		if(isDataSet && isTypeSet && isUniqueNumberSet)
		{
			try
			{
				requestThread.start();
			}
			catch(IllegalThreadStateException e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
	
	public byte[] getByteResponseData()
	{
		return responseByteData;		
	}
	
	public String getStringResponseData()
	{
		return responseStringData;
	}
	
	public String getResponseType()
	{
		return responseType;
	}
	
	public String getResponseUniqueNumber()
	{
		return responseUniqueNumber;
	}
	public String getResponseOrder()
	{
		return responseOrder;
	}
}
