package com.rhythmicsoftware.cmd.utils;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.rhythmicsoftware.cmd.utils.OauthConnector;
import com.rhythmicsoftware.cmd.utils.RestConnector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public void testApp() throws ClientProtocolException, IOException
    {
    	
    	RestConnector restConnector = new RestConnector("http://www.apache.org/",null);
    	boolean bResult = restConnector.request("dummy", "dummy", "", null);
    	
        assertTrue( bResult );
        
    	OauthConnector oauthConnector = new OauthConnector("http://www.apache.org/",null);
    	bResult = oauthConnector.request("dummy", "dummy", "", null);
    	
    	assertTrue( bResult );
    	
//    	assertTrue(true);
    }
}
