package com.boomi.connector.PetStore.client;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.Closeable;
import java.io.IOException;

public class RESTClient implements Closeable {

    private final CloseableHttpClient _client;
    private final HttpClientContext _context;
    
    public RESTClient(CloseableHttpClient client, HttpClientContext context){
        _client = client;
        _context = context;
        
    }
    
    public void close() throws IOException {
        _client.close();
    }

    public CloseableHttpResponse executeRequest(HttpUriRequest request) throws Exception{
        return _client.execute(request,_context);
    }
}
