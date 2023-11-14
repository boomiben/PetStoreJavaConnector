package com.boomi.connector.PetStore.operations;

import java.net.HttpURLConnection;
import java.util.List;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;

import com.boomi.connector.PetStore.PetStoreConnection;
import com.boomi.connector.api.ObjectData;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.OperationStatus;
import com.boomi.connector.api.RequestUtil;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.api.UpdateRequest;
import com.boomi.connector.util.BaseUpdateOperation;

public class CreateOperation extends BaseUpdateOperation
{
    private static Integer MAX_BATCH_SIZE = 1000;

    public CreateOperation(PetStoreConnection conn)
    {
        super(conn);
    }

    @Override
    protected void executeUpdate(UpdateRequest request, OperationResponse response)
    {
        // ... get user credentials (same as GET example) ...
        String uri = getConnection().getBaseURL() + "/" + getContext().getObjectTypeId();
        HttpUriRequest httpRequest = RequestBuilder.create("POST").setUri(uri).build();

        // handle new objects in batches of at most MAX_BATCH_SIZE
        for(List<ObjectData> requestDataBatch : RequestUtil.pageIterable(request, MAX_BATCH_SIZE,
                                                                         getContext().getConfig())) {

            // ... compile a batch of request elements into a big xml doc ...
            // ... Make POST request to requestUrl using given credentials ...
            // ... grab returned status code ...
            int httpStatusCode = ...;

            // indicate status of the operation
            OperationStatus status = ((httpStatusCode == HttpURLConnection.HTTP_OK) ?
                                      OperationStatus.SUCCESS : OperationStatus.FAILURE);
            ResponseUtil.addEmptyResults(response, requestDataBatch, status, String.valueOf(httpStatusCode), null);
        }
    }

    @Override
    public PetStoreConnection getConnection() 
    {
        return (PetStoreConnection) super.getConnection();
    }
}
