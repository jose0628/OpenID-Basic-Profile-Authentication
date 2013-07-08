package myfilter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import java.util.logging.*;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

public class MyFilter implements javax.servlet.Filter {
    private String redirectPage;
    private ServletContext servletContext;
    private Logger log;
    
    public MyFilter(){
        super();
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
        servletContext = filterConfig.getServletContext();
        redirectPage = filterConfig.getInitParameter("Redirect-Page");
        log = Logger.getLogger(MyFilter.class.getName());
        System.out.println("init method of filter"+"\n");
    }

    public void doFilter(   ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
    	
    	
    	
    	System.out.println("Arguments for the Do filter"+"\n");
    	System.out.println("req: "+req);
    	System.out.println("res: "+res);
    	System.out.println("filterChain: "+filterChain+"\n");
    	
    	System.out.println("Do Filter method inside"+"\n");
    	
        HttpServletRequest httpReq   = (HttpServletRequest)req;
        HttpServletResponse httpRes  = (HttpServletResponse)res;
        
        System.out.println("Http request: "+httpReq);
        System.out.println("http response: "+httpRes);
        
        
        HttpSession session = httpReq.getSession();
                
        System.out.println("session is: "+session+"\n");
        System.out.println("Give me the address" +httpReq.getRemoteAddr());
        System.out.println("GET PAth info "+httpReq.getPathInfo());
        System.out.println("GET PAth Trans "+httpReq.getPathTranslated());
        System.out.println("GET Query String "+httpReq.getQueryString());
        System.out.println("GET Query String "+httpReq.getQueryString());
        System.out.println("GET URI "+httpReq.getRequestURI());
        System.out.println("GET URL "+httpReq.getRequestURL());
        System.out.println("GET SERVLET PATH " +httpReq.getServletPath()+"\n");
        
        System.out.println("GET HEADER NAMES "+httpReq.getHeaderNames());

        //TODO: We have to get a secret key generated randomly and compare it and not static 
        if(httpReq.getParameterValues("secretkey:ELCA")!=null){
            httpRes.sendRedirect(redirectPage);
            
            
            System.out.println("GRANTING ACCESS TO ELCA " + httpRes+"\n");
        }        
        else{
        	System.out.println("no right password in the ELSE"+"\n");//
        	PrintWriter out = httpRes.getWriter();
            CharResponseWrapper wrapper = new CharResponseWrapper(httpRes);//

            filterChain.doFilter(httpReq, wrapper);
            
            System.out.println("The content type is "+wrapper.getContentType());
                        
            System.out.println("if(wrapper.getContentType() != null && wrapper.getContentType().indexOf(text/html)!=-1)"+"\n");
            
            
            if(wrapper.getContentType() != null && wrapper.getContentType().indexOf("text/html")!=-1) {
            	System.out.println("Inside of IF to write in index.html file");
                CharArrayWriter caw = new CharArrayWriter();
                caw.write(wrapper.toString().substring(0, wrapper.toString().indexOf("</body>")-1));
                caw.write("<h1>You are not Authorized to access</h1>");
                caw.write("\n</body></html>");
                httpRes.setContentLength(caw.toString().length());
                out.write(caw.toString());
            }
            else{
            	
            	System.out.println("skip writting index.html file");
            	
                out.write(wrapper.toString());
            }
            out.close();
        }
    }

    public void destroy(){
    }

}

class CharResponseWrapper extends HttpServletResponseWrapper {
    private CharArrayWriter output;

    public String toString() {
        return output.toString();
    }
    public CharResponseWrapper(HttpServletResponse response) {
        super(response);
        output = new CharArrayWriter();
    }
    public PrintWriter getWriter() {
        return new PrintWriter(output);
    }
}