package com.yedianchina.tools;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
	public static String getRandom(){
		 int[] a = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	        int loc = 0;
	        for (int i = 0; i < 4; i ++){           
	            loc = (int)(Math.random()*19); 
	            int temp = a[i];
	            a[i] = a[loc];
	            a[loc] = temp;            
	        }
	        String retVal="";
	        for (int i = 0; i < 4; i ++){
	        	retVal=retVal+a[i];
	            //System.out.print(a[i] + "  ");
	        }  
	        return retVal;
	}
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}