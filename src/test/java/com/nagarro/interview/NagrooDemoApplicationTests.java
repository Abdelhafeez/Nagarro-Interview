package com.nagarro.interview;

import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class NagrooDemoApplicationTests 
{
    @LocalServerPort
	protected
    int randomServerPort;
     
    @Test
	public void isIntialized() throws URISyntaxException 
	{ 
    //Dummy intialization test
	Assert.assertEquals(true, true);

    
	}
    
    
}
