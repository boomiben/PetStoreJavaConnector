package com.boomi.connector.PetStore.operations;

import com.boomi.connector.PetStore.PetStoreConnection;
import com.boomi.connector.PetStore.client.RESTClient;
import com.boomi.connector.api.DeleteRequest;
import com.boomi.connector.api.ObjectIdData;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.PayloadUtil;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.util.BaseDeleteOperation;
import com.boomi.util.IOUtil;

import java.util.Iterator;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

public class DeleteOperation extends BaseDeleteOperation {

    public DeleteOperation(PetStoreConnection connection) {
        super(connection);
    }

    @Override
    public PetStoreConnection getConnection() {
        return (PetStoreConnection) super.getConnection();
    }

    @Override
    protected void executeDelete(DeleteRequest request, OperationResponse operationResponse) {

        // "request" param is iterable. Loop through with iterator (cleaner solution
        // than using 2 nested FOR loops like with CreateOperation)
        Iterator<ObjectIdData> it = request.iterator();
        while (it.hasNext()) {
            ObjectIdData objectIdData = (ObjectIdData) it.next();

            CloseableHttpResponse response = null;
            RESTClient client = null;
            try {
                // Create the request
                String uri = getConnection().getBaseURL() + "/" + (getContext()).getObjectTypeId() + "/"
                        + objectIdData.getObjectId();
                HttpUriRequest httpRequest = RequestBuilder.create("DELETE").setUri(uri).build();
                client = getConnection().getRESTClient();
                response = client.executeRequest(httpRequest);
                if (response.getEntity().getContentLength() > 0) {
                    // Object found. Display Object
                    ResponseUtil.addResultWithHttpStatus(operationResponse, objectIdData,
                            response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(),
                            PayloadUtil.toPayload(response.getEntity().getContent()));
                } else {
                    // Object not found.
                    ResponseUtil.addEmptySuccess(operationResponse, objectIdData,
                            String.valueOf(response.getStatusLine().getStatusCode()));
                }
            } catch (Exception e) {
                ResponseUtil.addExceptionFailure(operationResponse, objectIdData, e);
            } finally {
                IOUtil.closeQuietly(response, client);
            }
        }
    }
}
