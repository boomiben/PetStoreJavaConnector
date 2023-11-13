package com.boomi.connector.PetStoreConnector;

import com.boomi.connector.api.BrowseContext;
import com.boomi.connector.api.Browser;
import com.boomi.connector.api.Operation;
import com.boomi.connector.api.OperationContext;
import com.boomi.connector.util.BaseConnector;

public class PetStoreConnector extends BaseConnector
{
    public PetStoreConnector() {
    }

    @Override
    public Browser createBrowser(BrowseContext context)
    {
        return new PetStoreBrowser(createConnection(context));
    }

    // @Override
    // protected Operation createGetOperation(OperationContext context)
    // {
    //     return new PetStoreGetOperation(createConnection(context));
    // }

    // @Override
    // protected Operation createUpdateOperation(OperationContext context)
    // {
    //     return new PetStoreUpdateOperation(createConnection(context));
    // }

    // @Override
    // protected Operation createCreateOperation(OperationContext context)
    // {
    //     return new PetStoreCreateOperation(createConnection(context));
    // }
 
     private PetStoreConnection createConnection(BrowseContext context) 
     {
       return new PetStoreConnection(context)
     }

}
