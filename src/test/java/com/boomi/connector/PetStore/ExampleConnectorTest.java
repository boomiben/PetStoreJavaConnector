package com.boomi.connector.PetStore;

import com.boomi.connector.testutil.ConnectorTester;
import com.boomi.connector.testutil.SimpleOperationResult;
import com.boomi.connector.util.BaseConnector;
import com.boomi.connector.api.*;
import com.boomi.connector.util.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boomi.connector.*;

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
        tester.setOperationContext(OperationType.GET, connProps, null, "pet", null);

        // ... setup the expected output ...
        //List<SimpleOperationResult> expectedResults = ...;
        List<SimpleOperationResult> results = tester.executeGetOperation("9223372016900089561");
        System.out.println(results);
        //tester.testExecuteGetOperation("9223372016900089561", expectedResults);
        System.out.println("done");
    }
}