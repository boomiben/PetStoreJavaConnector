package com.boomi.connector.PetStore.operations;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.InputStreamEntity;

import com.boomi.connector.PetStore.PetStoreConnection;
import com.boomi.connector.PetStore.client.RESTClient;
import com.boomi.connector.api.ObjectData;
import com.boomi.connector.api.OperationResponse;
import com.boomi.connector.api.PayloadUtil;
import com.boomi.connector.api.RequestUtil;
import com.boomi.connector.api.ResponseUtil;
import com.boomi.connector.api.UpdateRequest;
import com.boomi.connector.util.BaseUpdateOperation;
import com.boomi.util.IOUtil;

public class CreateOperation extends BaseUpdateOperation {
    private static Integer MAX_BATCH_SIZE = 1000;

    public CreateOperation(PetStoreConnection conn) {
        super(conn);
    }

    @Override
    protected void executeUpdate(UpdateRequest request, OperationResponse operationResponse) {
        String uri = getConnection().getBaseURL() + "/" + (getContext()).getObjectTypeId();
        CloseableHttpResponse response = null;
        RESTClient client = null;

        for (List<ObjectData> requestDataBatch : RequestUtil.pageIterable(request, MAX_BATCH_SIZE, getContext().getConfig())) {
            // Create the request for each item in the batch
            // handle new objects in batches of at most MAX_BATCH_SIZE
            // NOTE: "requestDataBatch" is a List/array of requests that can contain multiple documents UP TO MAX_BATCH_SIZE
            //  Therefore, we must iterate through requestDataBatch
            
            for (ObjectData objectData : requestDataBatch) {
                try {
                    // Pass payload into request
                    InputStreamEntity entity = new InputStreamEntity(objectData.getData());
                    HttpUriRequest httpRequest = RequestBuilder.create("POST")
                                                                .setUri(uri)
                                                                .setEntity(entity)
                                                                // TODO: removing these setHeader() statements causes a 404 Bad Request error -- but should this be hardcoded here?
                                                                // Idea: get Content Type from somewhere else?
                                                                // See "operation properties" referenced: https://bitbucket.org/officialboomi/swagger-openapi-framework-and-connectors/src/master/src/main/java/com/boomi/swaggerframework/swaggeroperations/SwaggerExecuteOperation.java
                                                                //Map<String,String> headers = this.getHeaderParameters(opProps, input.getDynamicOperationProperties());
                                                                .setHeader("Accept", "application/json")
                                                                .setHeader("Content-type", "application/json")
                                                                .build();

                    client = getConnection().getRESTClient();
                    response = client.executeRequest(httpRequest);

                    if (response.getEntity().getContentLength() > 0) {
                        // Object found. Display Object
                        ResponseUtil.addResultWithHttpStatus(operationResponse, objectData,
                                response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(),
                                PayloadUtil.toPayload(response.getEntity().getContent()));
                    } else {
                        // Object not found.
                        ResponseUtil.addEmptySuccess(operationResponse, objectData,
                                String.valueOf(response.getStatusLine().getStatusCode()));
                    }

                } 
                catch (Exception e) {
                    ResponseUtil.addExceptionFailure(operationResponse, objectData, e);
                } finally {
                    IOUtil.closeQuietly(response, client);
                }
            }
        }
    }

    @Override
    public PetStoreConnection getConnection() {
        return (PetStoreConnection) super.getConnection();
    }
}
