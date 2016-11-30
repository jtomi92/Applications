package com.jtech.apps.hcm;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;


@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Autowired
	DataSource dataSource;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select user_name,user_password, enabled from user_profiles where user_name=?")
				.authoritiesByUsernameQuery(
						"SELECT up.USER_NAME, g.GROUP_NAME FROM USER_PROFILES up, GROUPS g WHERE up.group_id = g.group_id AND up.user_name=?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()
		.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST,"/register").permitAll()
			.antMatchers(HttpMethod.GET,"/register").permitAll()
			.antMatchers("/console")
				.access("hasRole('USER') or hasRole('ADMIN')").anyRequest()
				.permitAll()
				.and()
					.formLogin().loginPage("/login")
					.usernameParameter("username")
					.passwordParameter("password")
					.defaultSuccessUrl("/console")
					.failureUrl("/login?error")
				.and()
					.logout().invalidateHttpSession(true)            
					.logoutSuccessUrl("/login?logout")
				
				.and()
					.exceptionHandling()
					.accessDeniedPage("/403").and().csrf();
				

	}
}