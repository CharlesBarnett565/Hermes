package onlineShop;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.context.annotation.Bean;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	protected void configure(HttpSecurity http) throws Exception {// authorization
		http
			.csrf().disable()
			.formLogin()
				.loginPage("/login")
				
			.and()// .and()分隔不同的逻辑功能，前面是login,后面是authorization
			.authorizeRequests()
			.antMatchers("/cart/**").hasAuthority("ROLE_USER")//访问购物车的话必须是登陆过的用户;
			.antMatchers("/get*/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")//哪个url需要哪个权限才能访问;
			.antMatchers("/admin*/**").hasAuthority("ROLE_ADMIN")//一个***?????
			.anyRequest().permitAll()//剩下的任意权限就可以访问;
			.and()
			.logout()
				.logoutUrl("/logout");
			
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {// authentication
		auth
			.inMemoryAuthentication().withUser("stefanlaioffer@gmail.com").password("123").authorities("ROLE_ADMIN");//在系统里面创建了一个用户来给他权限，便于测试;相当于跟用户root user
		
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")// 这里是在数据库中找人，是不是已经注册的用户，是不是enabled;
			.authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");//这句话就是在authority用户里面找，看这个用户是不是特权用户：
		
	}

       @SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
}
