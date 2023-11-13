package com.boomi.connector.PetStoreConnector.operations;

import com.boomi.connector.PetStoreConnector.PetStoreConnection;
import com.boomi.connector.PetStoreConnector.client.RESTClient;
import com.boomi.connector.api.GetRequest;
import com.boomi.connector.api.ObjectIdData;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.PayloadUtil;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.util.BaseGetOperation;
import com.boomi.util.IOUtil;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

public class GetOperation extends BaseGetOperation {

    public GetOperation(PetStoreConnection connection) {
        super(connection);
    }

    @Override
    public PetStoreConnection getConnection() {
        return (PetStoreConnection) super.getConnection();
    }
    
    protected void executeGet(GetRequest request, OperationResponse operationResponse) {
        //Fetch the object ID
        ObjectIdData objectId = request.getObjectId();
        CloseableHttpResponse response = null;
        RESTClient client = null;
        try {
            // Create the request            
            String uri = getConnection().getBaseURL() + "/" + (getContext()).getObjectTypeId() + "/" + objectId
                    .getObjectId();
            HttpUriRequest httpRequest = RequestBuilder.create("GET").setUri(uri).build();
            client = getConnection().getRESTClient();
            response = client.executeRequest(httpRequest);
            if (response.getEntity().getContentLength() > 0) {
                // Object found. Display Object
                ResponseUtil.addResultWithHttpStatus(operationResponse, objectId,
                        response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(),
                        PayloadUtil.toPayload(response.getEntity().getContent()));
            } else {
                // Object not found. 
                ResponseUtil.addEmptySuccess(operationResponse, objectId,
                        String.valueOf(response.getStatusLine().getStatusCode()));
            }
        } catch (Exception e) {
            ResponseUtil.addExceptionFailure(operationResponse, objectId, e);
        } finally {
            IOUtil.closeQuietly(response, client);
        }
    }
}
