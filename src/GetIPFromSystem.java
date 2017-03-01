
import org.apache.commons.lang3.*;
import java.io.*;

public class GetIPFromSystem {

	public static final String LAN_ADAPTER = "Ethernet adapter Ethernet";
	public static final String LAN_ADAPTER_LINUX = "eth";
	public static final String WIRELESS_LAN_ADAPTER = "Wireless LAN adapter Wi-Fi";
	
	public static final String WINDOWS_COMMAND = "ipconfig";
	public static final String LINUX_COMMAND = "ifconfig";
	
	private static boolean STD_ERROR_RETURNED_ERROR = false;
	private static boolean EXCEPTION_OCCURRED = false;
	
	private static String runExecCommand(String command) {
		
		try {
			   Process p = Runtime.getRuntime().exec(command);
			   
			   BufferedReader stdInput = new BufferedReader(new 
		                 InputStreamReader(p.getInputStream()));

		       BufferedReader stdError = new BufferedReader(new 
		                 InputStreamReader(p.getErrorStream()));

		       // read the output from the command
		       //System.out.println("Here is the standard output of the command:\n");
		       String s =  null;
		       StringBuffer sb = new StringBuffer();
		       while ((s = stdInput.readLine()) != null) {
		    	   // For Windows add a line return before next header 
		    	   // Windows indents details 2 spaces.
		    	   if(SystemUtils.IS_OS_WINDOWS) {
		    		  if(s.length() > 2 && !s.substring(0,2).equals("  ")) {
		    	         sb.append("\n" + s + "\n");
		    		  }//if//
		    		  else {
		    	        if(s != null & s.length() > 0) {
		    		       sb.append(s + "\n");
		    	        }//if//
		    		  }//if//
		    	   }//if//
		               //System.out.println(s);
		    	   else {
		    		   sb.append(s + "\n");
		    	   }//if//
		       }//while//
		       if(sb.toString() != null) {
		          return sb.toString();
		       }//if//
		            
		       // read any errors from the attempted command
		       //System.out.println("Here is the standard error of the command (if any):\n");
		       StringBuffer sb2 = new StringBuffer();	   
		       while ((s = stdError.readLine()) != null) {
		    	   if(s != null)
		    		   sb2.append(s + "\n");
		           //System.out.println(s);
		       }//while//
		       if(sb2.toString().length() > 0) {
		    	   STD_ERROR_RETURNED_ERROR = true;
		    	   return sb2.toString();
		       }//if//
			}//try//
			catch (IOException e)
			{
				EXCEPTION_OCCURRED = true;
				return e.toString();
			}//catch//
		return null;
	}//runExecCommand
	
	private static String getLanAddressWindows(String searchString) {
		   
		   StringBuffer sb = new StringBuffer();
		   
		   int i = 0;
		   while(searchString.indexOf(LAN_ADAPTER, i) >=0)
		   {
			      int j = searchString.indexOf(LAN_ADAPTER, i);
			      int j2 = searchString.indexOf("\n\n", j);
                  if( j >= 0 && j2 >= 0) {
        	          sb.append(searchString.substring(j,j2) + "\n\n");
                  }//if//
                  i = j + 1;
		   }       
		return sb.toString();
	}//getLanAddress//
	
	private static String getLanAddressLinux(String searchString) {
		String lanAddress = null;
		//System.out.println("The search string is " + searchString);
		
           int i = searchString.indexOf(LAN_ADAPTER_LINUX);
           System.out.println("The search string is " + " " + i);
           int l = searchString.indexOf("\n\n", i);
           if( i >= 0 && l >= 0)
        	   lanAddress = searchString.substring(i,l);
           else
        	   lanAddress = "Lan adapter disconnected";
	            
		return lanAddress;
	}//getLanAddressLinux//
	
	private static String getWirelessLanAddressWindows(String searchString) {
		
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		while(searchString.indexOf(WIRELESS_LAN_ADAPTER, i) >=0)
		{
			int j = searchString.indexOf(WIRELESS_LAN_ADAPTER, i);
			int j2 = searchString.indexOf("\n\n", j);
            if( j >= 0 && j2 > 0) {
  	          sb.append(searchString.substring(j,j2) + "\n\n");
            }
            i = j + 1;
		}
		return sb.toString();
	}//getWirelessLanAddressWindows//
	
	public static void main(String[] args) {

		String resultText = null;

		if(SystemUtils.IS_OS_LINUX) {
			System.out.println("System is Linux");
			resultText = runExecCommand(LINUX_COMMAND);
			if(STD_ERROR_RETURNED_ERROR) {
				System.out.println("Standard Error Text:" + resultText);
			}//if//
			else if(EXCEPTION_OCCURRED) {
				System.out.println("Exception occurred: " + "\"" + resultText + "\"");
			}//else
			else {
				System.out.println(getLanAddressLinux(resultText));
			}//else
		}//if//
		
		if(SystemUtils.IS_OS_WINDOWS) {
			System.out.println("System is Windows");
			resultText = runExecCommand(WINDOWS_COMMAND);
			if(STD_ERROR_RETURNED_ERROR) {
				System.out.println("Standard Error Text:" + resultText);
			}//if//
			else if(EXCEPTION_OCCURRED) {
				System.out.println("Exception occurred: " + "\"" + resultText + "\"");
			}//else
			else {
				System.out.println(getLanAddressWindows(resultText));
				System.out.println(getWirelessLanAddressWindows(resultText));
			}//else
		}//if
	}//main
}//GetIPFromSystem//
