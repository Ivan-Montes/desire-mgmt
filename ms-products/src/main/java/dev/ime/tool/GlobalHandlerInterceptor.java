package dev.ime.tool;

import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GlobalHandlerInterceptor implements HandlerInterceptor {

	private final Logger logger;
	
	public GlobalHandlerInterceptor(Logger logger) {
		super();
		this.logger = logger;
	}
	
    // This method is called before the handler method is executed. It returns a boolean value indicating whether the execution chain should continue or not.
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
		String msg = ("## [preHandle] ## [" + String.format( SomeConstants.FORMAT_TIME ,System.currentTimeMillis()) + "][" + request.getMethod() + "][" + request.getRequestURI() + "]");
        logger.info(msg);
        
        return true;
    }

    // This method is called after the handler method is executed, but before the view is rendered. It allows us to modify the ModelAndView object before it is rendered.
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	
    	String msg = ("## [postHandle] ## [" + String.format(SomeConstants.FORMAT_TIME, System.currentTimeMillis()) + "][" + request.getMethod() + "][" + request.getRequestURI() + "]");
    	logger.info(msg);
        
    }

    // This method is called after the view is rendered. It allows us to perform any cleanup tasks.
    @Override 
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	
        String msg = ("## [afterCompletion] ## [" + String.format(SomeConstants.FORMAT_TIME, System.currentTimeMillis()) + "][" + request.getMethod() + "][" + request.getRequestURI() + "][STATUS:" + response.getStatus() + "]");
    	logger.info(msg);

    }
    
}
