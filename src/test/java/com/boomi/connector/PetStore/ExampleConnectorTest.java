package com.boomi.connector.PetStore;

import com.boomi.connector.testutil.ConnectorTester;
import com.boomi.connector.testutil.SimpleOperationResult;
import com.boomi.connector.api.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleConnectorTest {
    
    public static void main(String[] args) {
        System.out.println("testing");

        try {
            testGetOperation();
        }
        catch (Exception e) {

        }
    }

    public static void testGetOperation() throws Exception
    {
        PetStoreConnector connector = new PetStoreConnector();
        ConnectorTester tester = new ConnectorTester(connector);

        // setup the operation context for a GET operation on an object with type "SomeType"
        Map<String, Object> connProps = new HashMap<>();
        connProps.put("url","https://petstore3.swagger.io/api/v3");

        // Test PET GET
        tester.setOperationContext(OperationType.GET, connProps, null, "pet", null);
        List<SimpleOperationResult> resultsPet = tester.executeGetOperation("9223372016900089561");
        System.out.println(resultsPet);

        // Test Store/Order
        tester.setOperationContext(OperationType.GET, connProps, null, "store/order", null);

        List<SimpleOperationResult> resultsStoreOrder = tester.executeGetOperation("10");
        System.out.println(resultsStoreOrder);
        
        System.out.println("done!");
    }
}