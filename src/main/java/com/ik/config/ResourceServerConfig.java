package com.ik.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource_id";

/*	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}*/
	
	@Autowired
	private TokenStore tokenStore;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		/*http.anonymous().disable().authorizeRequests().antMatchers("/users/**").access("hasRole('ADMIN')").and()
				.exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());*/
		
		 http.
         anonymous().disable()
         .authorizeRequests()
         .antMatchers("/oauth/**").permitAll()
         .antMatchers("/app.html").permitAll()
         .antMatchers("/*.js").permitAll()
         .antMatchers("/*.css").permitAll()
         .antMatchers("/registration*").permitAll()
         //.antMatchers("/users").access("hasRole('ADMIN')")
         .antMatchers("/user/**").fullyAuthenticated()
         .antMatchers("/users").fullyAuthenticated()
         .anyRequest().authenticated()
         .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	    resources.resourceId(RESOURCE_ID).tokenStore(tokenStore);
	}
}