package com.yzc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("package com.yzc.support.log")
public class AspectJConfig {
	
//	@Bean
//	public OperationLog OperationLog(){
//		
//		return new OperationLog();
//	}

}
