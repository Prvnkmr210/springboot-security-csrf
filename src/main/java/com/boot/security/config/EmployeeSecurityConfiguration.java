package com.boot.security.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@Configuration
@EnableWebSecurity
public class EmployeeSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Bean (name = "MySQLDataSource")
	public DataSource getMySQLDataSource(){
		DataSourceBuilder dataSourcebuilder = DataSourceBuilder.create();
		dataSourcebuilder.url("jdbc:mysql://localhost:3306/testmysqldb");
		dataSourcebuilder.username("root");
		dataSourcebuilder.password("leetcode@3");
		dataSourcebuilder.driverClassName("com.mysql.jdbc.Driver");
		return dataSourcebuilder.build();
	}	
	
	//Enable jdbc authentication for persistent database
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(getMySQLDataSource());
    }
    
    @Bean
	public JdbcUserDetailsManager jdbcUserDetailsManager() throws Exception {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
		jdbcUserDetailsManager.setDataSource(getMySQLDataSource());
		return jdbcUserDetailsManager;
	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/resources/**");
//	}
	
//	Authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
								.antMatchers("/register").permitAll()
								.antMatchers("/welcome").hasAnyRole("USER", "ADMIN")
								.antMatchers("/getEmployees").hasAnyRole("USER", "ADMIN")
								.antMatchers("/addNewEmployee").hasAnyRole("ADMIN")
								.anyRequest().authenticated().and()
								.formLogin().loginPage("/login").permitAll().and()
								.logout().permitAll();

		http.csrf().disable();
	}
	
	// Authentication
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
		authenticationMgr.inMemoryAuthentication()
			.withUser("admin").password("{noop}admin").authorities("ROLE_USER").and()
			.withUser("javainuse").password("{noop}javainuse").authorities("ROLE_USER", "ROLE_ADMIN");
		// Adding {noop} solved all the problem
		// https://mkyong.com/spring-boot/spring-security-there-is-no-passwordencoder-mapped-for-the-id-null/
	}


}