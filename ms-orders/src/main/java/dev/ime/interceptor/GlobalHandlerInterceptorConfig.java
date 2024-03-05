package dev.ime.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class GlobalHandlerInterceptorConfig implements WebMvcConfigurer {
	 
	 private final GlobalHandlerInterceptor interceptor; 	 
	 
	 public GlobalHandlerInterceptorConfig(GlobalHandlerInterceptor interceptor) {
		super();
		this.interceptor = interceptor;
	}

	@Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(interceptor).addPathPatterns("/api/**");
	    }
}
