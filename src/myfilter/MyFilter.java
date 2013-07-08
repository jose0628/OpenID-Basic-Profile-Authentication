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
    }

    public void doFilter(   ServletRequest req, 
                            ServletResponse res, 
                            FilterChain filterChain)
        throws IOException, ServletException {

        HttpServletRequest httpReq    = (HttpServletRequest)req;
        HttpServletResponse    httpRes   = (HttpServletResponse)res;
        
        HttpSession session = httpReq.getSession();

        if(httpReq.getParameterValues("redirectme")!=null){
            httpRes.sendRedirect(redirectPage);
        }        
        else{
            PrintWriter out = httpRes.getWriter();
            CharResponseWrapper wrapper = new CharResponseWrapper(httpRes);

            filterChain.doFilter(httpReq, wrapper);
            
            if(wrapper.getContentType() != null && wrapper.getContentType().indexOf("text/html")!=-1) {
                CharArrayWriter caw = new CharArrayWriter();
                caw.write(wrapper.toString().substring(0, wrapper.toString().indexOf("</body>")-1));
                caw.write("<p>\nHello There</p>");
                caw.write("\n</body></html>");
                httpRes.setContentLength(caw.toString().length());
                out.write(caw.toString());
            }
            else{
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