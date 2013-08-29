/*******************************************************************************

The present filter follows the OpenId Connect standard and specifically the profile:

OpenID Connect Basic 1.0

http://openid.net/specs/openid-connect-basic-1_0-28.html

Author: Jose Mancera
******************************************************************************/


package myfilter;

//Here we import all the needed libraries

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;

// Here we start with the main class 

public class OpenID_Auth implements javax.servlet.Filter {
    	
	private String redirectPage;
    public OpenID_Auth(){
        super();
    }
    
    // Here it is the initialization function for the filter. Everytime that the filter is refreshed or reloaded it will print "init method of filter"
    //The paremeter "redirect page comes from the file web.xml
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig.getServletContext();
        redirectPage = filterConfig.getInitParameter("Redirect-Page");
        Logger.getLogger(OpenID_Auth.class.getName());
        System.out.println("init method of filter"+"\n");    
        
    }

    //This is the main method to make a filter
    public void doFilter(   ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
    	
    	//This is an optional print command, it can be erased
    	
    	System.out.println("Obtener el Parametros MAP " + req.getParameterMap() + "\n"); //Obtener el Parametros MAP {ZURI=[Ljava.lang.String;@31d5d0fc}
    	
    	    	    	
    	/*/This are optional print commands, they can be erased
    	System.out.println("Arguments for the Do filter"+"\n");
    	System.out.println("req: "+ req );
    	System.out.println("res: "+res);
    	System.out.println("filterChain: "+filterChain+"\n");
    	System.out.println("Do Filter method inside"+"\n");
    	*/////////////////////////////////////////////////////////
    	HttpServletRequest httpReq   = (HttpServletRequest)req;
        HttpServletResponse httpRes  = (HttpServletResponse)res;
        /*////////////////////////////////////////////////////////
        System.out.println("Http request: "+httpReq);
        System.out.println("http response: "+httpRes+"\n");
        *////////////////////////////////////////////////////////       
        HttpSession session = httpReq.getSession();
                
        /* Optional parameters that can be printed for future versions       
        System.out.println("SESSION is: "+session+"\n");
        System.out.println("Give me the address" +httpReq.getRemoteAddr());
        System.out.println("GET PAth info "+httpReq.getPathInfo());
        System.out.println("GET PAth Trans "+httpReq.getPathTranslated());
        System.out.println("GET Query String "+httpReq.getQueryString()+"\n");
        System.out.println("GET URL "+httpReq.getRequestURL());
        System.out.println("GET URI "+httpReq.getRequestURI());
        System.out.println("GET SERVLET PATH " +httpReq.getServletPath()+"\n");
        System.out.println("GET HEADER NAMES "+httpReq.getHeaderNames()+"\n");
        */
        
        
        /*TODO:
         * For the moment the way that the filter works in the next way:
         * 
         * The web app or the login page is independent of this filter and the welcome page has to create a string with in the next format: 
         * 
         * Example for the user ELCA123
         * 
         * http://<IP or DOMAIN Of COMPUTER THAT HOST THE FILTER>:8080/Complete_Java_Filter_Example/openid/?ELCA123
         * 
         * Then the filter receive the query and with the user will add the Idp URL:
         * 
         * http://10.10.9.88:8080/openid-connect-server/authorize?response_type=code&client_id=ELCA123&redirect_uri=http%3A%2F%2F"IP_OF_THE_VIRTUAL_MACHINE"%3A8080%2FComplete_Java_Filter_Example%2Fopenid%2F&scope=openid
         * 
         * For the moment you have to specify manually the user and the IP of the filter machine. It can be modified in the future. The main reason that I did this was because the requirements for the integration or the 
         * welcome screen was not designed and due time constraints.
         * 
         *    
         *  We have to get a secret key generated randomly and compare it and not static 
        
        
        */
        //This forst if condition it does not have any use in the code, it only shows the principle that when the query has a parameter called ELCA, it displays a HTML webpage
        // It can be modified in the future for other purposes.
        if(httpReq.getParameterValues("ELCA")!=null){
            
        	
        	httpRes.sendRedirect("http://localhost:8080/Complete_Java_Filter_Example/index2.html");
                   	
        	System.out.println("DENTRO DEL IF ELCA " + httpRes +"\n");
        }
        // This function is waiting to receive the token and print it (This part apply for other Open ID Connect profiles that require a triple check - HERE ITS NOT THE CASE)
        // This function is not used for the moment but can be useful later to modify the filter
        else if (httpReq.getParameterValues("access_token")!=null){
            
        	//Here instead of print the token, you can add some extra step verifications
        	System.out.println("THERE IS A TOKEN " + httpReq.getQueryString() +"\n");
        }
       
        /*This is the option that will be called for the use ELCA123
         * You should notice that that you can make more efficient the method httpRes.sendRedirect, if you add more variables to construct the URL
         * 10.10.128.61 is in this case the ip of the machine that has hosted the filter
         * 
         * Notice that the method httpReq.getParameterValues in every if, checks the parameters that are arriving in every query to the filter, this is very usefu
         * to create other condition if they are needed.
        */
        else if (httpReq.getParameterValues("ELCA123")!=null){
                  
        	// We simulate the GET method using this Redirect
        	httpRes.sendRedirect(redirectPage+"authorize?response_type=code&client_id=ELCA123&redirect_uri=http%3A%2F%2F10.10.128.58%3A8080%2FComplete_Java_Filter_Example%2Fopenid%2F&scope=openid");
            
            System.out.println("Send the request to the IDP (redirect page) " + httpRes +"\n");
        }
   
        /*After the filter send the user ELCA123 to the Idp, This will answer with a provisional verification code which will be catch by the next block * 
    	 * 
    	 */
        
        else if (httpReq.getParameterValues("code")!=null){
            
        	
        	System.out.println("THERE IS A PROVISIONAL CODE (ANSWER FROM IDP) This is  " + httpReq.getQueryString()+"\n");
        	
        	/*This part its  a little bit tricky - A get method can be simulated by a redirect but according to the documentation we have to give back the code using a POST method.
        	 * There is no way to use a Redirect function to simulate a POST so the POST method to send the code is done in this way
        	 */
        	////////////////////////////////// INITIAL PARAMETERS
        	String CONTENT_TYPE = "text/html";
        	URL		 url;
        	URLConnection    urlConn;
        	DataOutputStream cgiInput;
        	/////////////////////////////////// Here dont forget to write the IP or domain of the filter
        	res.setContentType(CONTENT_TYPE);
        	String filter_url=URLEncoder.encode("http://10.10.128.58:8080/Complete_Java_Filter_Example/openid/", "UTF-8");
        	// URL of target page script.
        	
        	url = new URL(redirectPage+"token?grant_type=authorization_code&" + httpReq.getQueryString() + "&redirect_uri=" + filter_url);
        	urlConn = url.openConnection();
        	
        	urlConn.setDoInput(true);
        	urlConn.setDoOutput(true);
        	urlConn.setUseCaches(false);
        	urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        	// Here its very important to use base 64 for the password which is in this case in this format ELCA123:secret and this is equal to RUxDQTEyMzpzZWNyZXQ=
        	urlConn.setRequestProperty("Authorization", "Basic RUxDQTEyMzpzZWNyZXQ=");
        	        	
        	System.out.println("URL STRUCTURE " + urlConn+"\n");
        	        	
        	// Send POST output.
        	cgiInput = new DataOutputStream(urlConn.getOutputStream());      	
        	cgiInput.flush();
        	cgiInput.close();
        	
        	// reads the CGI response and print it inside the servlet content in order to create a temporal HTML tha shows the token
        	
        	BufferedReader cgiOutput = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        	PrintWriter servletOutput = res.getWriter();
        	
        	
        	
        	//This is the generation of the temporal HTML page that shows the token
        	//TODO Here according the basic profile, we just receive the token and it means that the user has access.
        	//In order to make it more complete you can increase the complexity receiving the token using an array and verifying the parts of the token.
        	//////////////////////////////////////////////////////////////////////////////////
        	servletOutput.print("<html><body><h1>This is the RESPONSE WITH TOKEN</h1><p />");
        	        			
        	String line = null;
        	while (null != (line = cgiOutput.readLine())){
        			
        		servletOutput.println(line);
        		        			
        	}
        			
        	cgiOutput.close();
        	
        	servletOutput.print("</body></html>");
        	servletOutput.close();
      	
       }
            //////////////////////////////////////////////////////////////////////////////////
        //Here there is a final criteria when we have errors in the query request          
        else{
        	 // Warning Message as an unsuccessful query
        	System.out.println("no right password syntax- NO FORWARD TO IDP"+"\n");//                                             
            System.out.println("+++++++++++++++++++++++++++++++++++ SORRY BAD SYNTAX IN THE REQUEST ++++++++++++++++++++++++++++++++++++++++ ");
                       
        }
    }

    public void destroy(){
    	
    }
}

