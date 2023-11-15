package com.boomi.connector.PetStore;

import com.boomi.connector.testutil.ConnectorTester;
import com.boomi.connector.testutil.SimpleOperationResult;
import com.boomi.connector.api.*;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleConnectorTest {
    
    public static void main(String[] args) {
        System.out.println("Starting tests...");

        try {
            testGetOperation();
            testCreateOperation();
        }
        catch (Exception e) {
            System.out.println("EXCEPTION OCCURRED! ..." + e.toString());
        }
        finally {
            System.out.println("Testing ended...");
        }
    }

    public static void testGetOperation() throws Exception
    {
        PetStoreConnector connector = new PetStoreConnector();
        ConnectorTester tester = new ConnectorTester(connector);

        // setup the operation context for a GET operation on an object with type "SomeType"
        Map<String, Object> connProps = new HashMap<String,Object>();
        connProps.put("url","https://petstore3.swagger.io/api/v3");

        // Test PET GET
        tester.setOperationContext(OperationType.GET, connProps, null, "pet", null);
        List<SimpleOperationResult> resultsPet = tester.executeGetOperation("1");
        System.out.println(resultsPet);

        // Test Store/Order
        tester.setOperationContext(OperationType.GET, connProps, null, "store/order", null);

        List<SimpleOperationResult> resultsStoreOrder = tester.executeGetOperation("10");
        System.out.println(resultsStoreOrder);
        
        System.out.println("testGetOperation(): done!");
    }

    public static void testCreateOperation() throws Exception
    {
        PetStoreConnector connector = new PetStoreConnector();
        ConnectorTester tester = new ConnectorTester(connector);

        Map<String, Object> connProps = new HashMap<String,Object>();
        connProps.put("url","https://petstore3.swagger.io/api/v3");
        
        Map<String, Object> opProps = new HashMap<String,Object>();
		
        //Set simulated payload for CREATE
		String payload = "{\n" + 
				"  \"id\": 9873598346,\n" + 
				"  \"name\": \"JDoggie3\",\n" + 
				"  \"status\": \"available\"\n" + 
				"}";

        tester.setOperationContext(OperationType.CREATE, connProps, opProps, "pet", null);
        List<InputStream> inputs = new ArrayList<InputStream>();
        inputs.add(new ByteArrayInputStream(payload.getBytes()));
        List <SimpleOperationResult> resultsPetCreate = tester.executeCreateOperation(inputs);
        //assertEquals("OK", resultsPetCreate.get(0).getMessage());
        //assertEquals("200",resultsPetCreate.get(0).getStatusCode());
        //assertEquals(1, resultsPetCreate.get(0).getPayloads().size());
        //String responseString = new String(resultsPetCreate.get(0).getPayloads().get(0));
        //System.out.println(responseString);
        System.out.println(resultsPetCreate);

        System.out.println("testCreateOperation(): done!");

    }

}