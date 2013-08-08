package myfilter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;


public class OpenID_Auth implements javax.servlet.Filter {
    	
	private String redirectPage;
    public OpenID_Auth(){
        super();
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig.getServletContext();
        redirectPage = filterConfig.getInitParameter("Redirect-Page");
        Logger.getLogger(OpenID_Auth.class.getName());
        System.out.println("init method of filter"+"\n");
        //System.out.println("THE REDIREC PAGE OF IDP "+redirectPage+"\n");
       
        
    }

    public void doFilter(   ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
    	
    	////////////////////////////////////////////////////////////////////////////////////////////
    	
    	System.out.println("Obtener el Parametros MAP " + req.getParameterMap() + "\n"); //Obtener el Parametros MAP {ZURI=[Ljava.lang.String;@31d5d0fc}
    	//System.out.println("Obtener el Parametros NAMES " + Arrays.asList(req.getParameterValues("ZURI")) + "\n"); //Obtener el Parametros NAMES [pepechacal]
    	////////////////////////////////////////////////////////////////////////////////////////////
    	
    	
    	    	
    	/////////////////////////////////////////////////////////////////////////////////////////////
    	System.out.println("Arguments for the Do filter"+"\n");
    	System.out.println("req: "+ req );
    	System.out.println("res: "+res);
    	System.out.println("filterChain: "+filterChain+"\n");
    	
    	System.out.println("Do Filter method inside"+"\n");
    	
        HttpServletRequest httpReq   = (HttpServletRequest)req;
        HttpServletResponse httpRes  = (HttpServletResponse)res;
        
        System.out.println("Http request: "+httpReq);
        System.out.println("http response: "+httpRes+"\n");
        ////////////////////////////////////////////////////////////////////////////////////////
        
        
        HttpSession session = httpReq.getSession();
                
        
        ////////////////////////////////////////////////////////////////////////////////////////        
        System.out.println("SESSION is: "+session+"\n");
        System.out.println("Give me the address" +httpReq.getRemoteAddr());
        System.out.println("GET PAth info "+httpReq.getPathInfo());
        System.out.println("GET PAth Trans "+httpReq.getPathTranslated());
        System.out.println("GET Query String "+httpReq.getQueryString()+"\n");
        System.out.println("GET URL "+httpReq.getRequestURL());
        System.out.println("GET URI "+httpReq.getRequestURI());
        System.out.println("GET SERVLET PATH " +httpReq.getServletPath()+"\n");
        
        System.out.println("GET HEADER NAMES "+httpReq.getHeaderNames()+"\n");
        ////////////////////////////////////////////////////////////////////////////////////////
        //TODO: We have to get a secret key generated randomly and compare it and not static 
        
        if(httpReq.getParameterValues("ELCA")!=null){
            
        	
        	httpRes.sendRedirect("http://localhost:8080/Complete_Java_Filter_Example/index2.html");
                   	
        	System.out.println("DENTRO DEL IF ELCA " + httpRes +"\n");
        }
        
        else if (httpReq.getParameterValues("access_token")!=null){
            
        	
        	System.out.println("THERE IS A TOKEN " + httpReq.getQueryString() +"\n");
        }
       
        else if (httpReq.getParameterValues("ELCA123")!=null){
            
        	
        	httpRes.sendRedirect(redirectPage+"authorize?response_type=code&client_id=ELCA123&redirect_uri=http%3A%2F%2F10.10.128.48%3A8080%2FComplete_Java_Filter_Example%2Fopenid%2F&scope=openid&state=af0ifjsldkj");
            
            System.out.println("Send the request to the IDP (redirect page) " + httpRes +"\n");
        }
   
        
        else if (httpReq.getParameterValues("code")!=null){
            
        	
        	System.out.println("+++++++++++++ THERE IS A CODE (ANSWER FROM IDP) +++++++++++++ " + Arrays.toString(httpReq.getParameterValues("code"))+"\n");
        	//System.out.println("+++++++++++++ THERE IS A state (ANSWER FROM IDP) +++++++++++++ " + Arrays.asList(httpReq.getParameterValues("state"))+"\n");
        	System.out.println("+++++++++++++ All the parameters SUPER ANSWER +++++++++++++ " + httpReq.getQueryString()+"\n");
        	
        	//TODO: we have to make a POST to send the CODE//////////////////////////////////////////////
        	/*
        	String filter_url=URLEncoder.encode("http://10.10.128.48:8080/Complete_Java_Filter_Example/openid/", "UTF-8");
        	
        	
        	httpRes.setHeader("Host", "http://10.10.9.88:8080/openid-connect-server");
        	httpRes.setHeader("Authorization", "Basic ZWxjYTEyMzpzZWNyZXQ=");
        	httpRes.setContentType("application/x-www-form-urlencoded");
        	httpRes.sendRedirect(redirectPage + "grant_type=authorization_code&" + httpReq.getQueryString() + "&redirect_uri=" + filter_url);
        	
        	/*
        	HttpClient client = new DefaultHttpClient();  
            HttpPost post = new HttpPost("http://www.mymi5.net/API/auth/login");   
            post.setHeader("Content-type", "application/json");
            post.setHeader("Accept", "application/json");
            JSONObject obj = new JSONObject();
            obj.put("username", "abcd");
            obj.put("password", "1234");
            post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
            HttpResponse response = client.execute(post);  
        	*/
        	
        	
        	
        	
        	String CONTENT_TYPE = "text/html";
        	URL		 url;
        	URLConnection    urlConn;
        	DataOutputStream cgiInput;
        	
        	res.setContentType(CONTENT_TYPE);
        	String filter_url=URLEncoder.encode("http://10.10.128.48:8080/Complete_Java_Filter_Example/openid/", "UTF-8");
        	// URL of target page script.
        	url = new URL(redirectPage+"token?grant_type=authorization_code&" + httpReq.getQueryString() + "&redirect_uri=" + filter_url);
        	urlConn = url.openConnection();
        	
        	urlConn.setDoInput(true);
        	urlConn.setDoOutput(true);
        	urlConn.setUseCaches(false);
        	urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        	urlConn.setRequestProperty("Authorization", "Basic RUxDQTEyMzpzZWNyZXQ=");
        	        	
        	System.out.println("URL STRUCTURE " + urlConn+"\n");
        	        	
        	// Send POST output.
        	cgiInput = new DataOutputStream(urlConn.getOutputStream());
        	
        	
        	
        	//String content = "?grant_type=authorization_code&" + httpReq.getQueryString() + "&redirect_uri=" + filter_url;

        	//cgiInput.writeBytes(content);
        	
        	//System.out.println("URL / ++++++++++++++++++++++++++++++++++++++++++++++++/ " + content+"\n");
        	
        	cgiInput.flush();
        	cgiInput.close();
        	
        	// reads the CGI response and print it inside the servlet content
        	
        	BufferedReader cgiOutput = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        	PrintWriter servletOutput = res.getWriter();
        	
        	servletOutput.print("<html><body><h1>This is the RESPONSE WITH TOKEN</h1><p />");
        	//System.out.println("<html><body><h1>This is the RESPONSE WITH TOKEN</h1><p />");
        			
        	String line = null;
        	while (null != (line = cgiOutput.readLine())){
        			
        		servletOutput.println(line);
        		//System.out.println(line+"\n");
        			
        	}
        			
        	cgiOutput.close();
        	
        	//System.out.println("</body></html>");
        	servletOutput.print("</body></html>");
        	servletOutput.close();
        	
        	
      	
       }
                        
        else{
        	        	
        	System.out.println("no right password syntax- NO FORWARD TO IDP"+"\n");//
        	        	
        	                                             
            System.out.println("+++++++++++++++++++++++++++++++++++ SORRY TRY AGAIN ++++++++++++++++++++++++++++++++++++++++ ");
                       
        }
    }

    public void destroy(){
    	
    }
}

