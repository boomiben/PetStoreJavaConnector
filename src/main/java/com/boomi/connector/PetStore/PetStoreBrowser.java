package com.boomi.connector.PetStore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.boomi.connector.api.ConnectorException;
import com.boomi.connector.api.ContentType;
import com.boomi.connector.api.ObjectDefinition;
import com.boomi.connector.api.ObjectDefinitionRole;
import com.boomi.connector.api.ObjectDefinitions;
import com.boomi.connector.api.ObjectType;
import com.boomi.connector.api.ObjectTypes;
import com.boomi.connector.util.BaseBrowser;
import com.boomi.util.ClassUtil;
import com.boomi.util.IOUtil;
import com.boomi.util.StreamUtil;

public class PetStoreBrowser extends BaseBrowser
{
    private static final String SCHEMA_PET = "/schema-pet.json";
    private static final String SCHEMA_STORE_ORDER = "/schema-store-order.json";
    private static final String UTF8 = "UTF-8";

    public PetStoreBrowser(PetStoreConnection conn)
    {    
        super(conn);
    }

    @Override
    public ObjectTypes getObjectTypes()
    {
        //String requestUrl = "http://www.example.com/service/type";
        // ... Make GET request to requestUrl ...
        // ... parse results into list ...
        List<String> returnedTypeNames = Arrays.asList(new String[]{"pet", "store/order"});

        // process returned list of type names
        ObjectTypes types = new ObjectTypes();
        for(String typeName : returnedTypeNames) {
            ObjectType type = new ObjectType();
            type.setId(typeName);
            type.setLabel(typeName);
            types.getTypes().add(type);
        }
        return types;
    }

    @Override
    public ObjectDefinitions getObjectDefinitions(String objectTypeId, Collection<ObjectDefinitionRole> roles)
    {
        // String requestUrl = "http://www.example.com/service/type/" + objectTypeId + "/def";
        // // ... Make GET request to requestUrl ...
        // // ... parse returned stream into DOM ...
        // Document schemaDoc = ...;

        // // create new definition
        // ObjectDefinition def = new ObjectDefinition();
        // def.setSchema(schemaDoc.getDocumentElement());
        // ObjectDefinitions defs = new ObjectDefinitions();
        // defs.getDefinitions().add(def);
        // return defs;

        ObjectDefinitions definitions = new ObjectDefinitions();
        switch (getContext().getOperationType()) {

            case GET:
                //Output has incoming data, no outgoing data
                definitions.getDefinitions().add(
                        new ObjectDefinition()
                                .withInputType(ContentType.NONE)
                                .withOutputType(ContentType.JSON)
                                .withJsonSchema(getJsonSchema(objectTypeId))
                                .withElementName(""));
                    
                break;
            // // output and input
            // case EXECUTE:
                
            //     ObjectDefinition inputDef = new ObjectDefinition()
            //             .withInputType( ContentType.JSON )
            //             .withOutputType( ContentType.NONE)
            //             .withJsonSchema(getJsonSchema())
            //             .withElementName("");
            //     definitions.getDefinitions().add(inputDef);

            //     definitions.getDefinitions().add(new ObjectDefinition()
            //             .withInputType(ContentType.NONE)
            //             .withOutputType(ContentType.NONE));
            //     break;
            default:
                throw new UnsupportedOperationException();
        }
        return definitions;
    }

    private static String getJsonSchema(String objectTypeId) {
        String schema;
        InputStream is = null;
        switch (objectTypeId) {
            case "pet":
                is = ClassUtil.getResourceAsStream(SCHEMA_PET);
                break;
            case "store/order":
                is = ClassUtil.getResourceAsStream(SCHEMA_STORE_ORDER);
                break;
            default:
                break;
        }
         
        try {
            schema = StreamUtil.toString(is, Charset.forName(UTF8));
        } catch (IOException ex) {
            throw new ConnectorException("Error reading schema", ex);
        } finally {
            IOUtil.closeQuietly(is);
        }
        return schema;
    }

    @Override
    public PetStoreConnection getConnection() 
    {
        return (PetStoreConnection) super.getConnection();
    }

}
