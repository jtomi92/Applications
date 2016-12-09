package com.jtech.apps.hcm;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;


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
		http
        .csrf()
            .disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST,"/register").permitAll()
			.antMatchers(HttpMethod.GET,"/register").permitAll()
			.antMatchers("/console","/myaccount")
				.access("hasRole('USER') or hasRole('ADMIN')").anyRequest()
				.permitAll()
				.and()
					.formLogin().successHandler(savedRequestAwareAuthenticationSuccessHandler())
					.loginPage("/login")
					.defaultSuccessUrl("/console")
					.failureUrl("/login?error")
					.usernameParameter("username")
					.passwordParameter("password")
				 				
				.and()
					.logout().invalidateHttpSession(true)
					.logoutSuccessUrl("/login?logout")
				.and().csrf()
				.and()
				.rememberMe().tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(1209600)
				.and()
					.exceptionHandling()
					.accessDeniedPage("/403");
	}
	
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}
	
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler
                savedRequestAwareAuthenticationSuccessHandler() {

               SavedRequestAwareAuthenticationSuccessHandler auth
                    = new SavedRequestAwareAuthenticationSuccessHandler();
		auth.setTargetUrlParameter("targetUrl");
		return auth;
	}
}