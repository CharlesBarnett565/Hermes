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
				
			.and()// .and()�ָ���ͬ���߼����ܣ�ǰ����login,������authorization
			.authorizeRequests()
			.antMatchers("/cart/**").hasAuthority("ROLE_USER")//���ʹ��ﳵ�Ļ������ǵ�½�����û�;
			.antMatchers("/get*/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")//�ĸ�url��Ҫ�ĸ�Ȩ�޲��ܷ���;
			.antMatchers("/admin*/**").hasAuthority("ROLE_ADMIN")//һ��***?????
			.anyRequest().permitAll()//ʣ�µ�����Ȩ�޾Ϳ��Է���;
			.and()
			.logout()
				.logoutUrl("/logout");
			
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {// authentication
		auth
			.inMemoryAuthentication().withUser("stefanlaioffer@gmail.com").password("123").authorities("ROLE_ADMIN");//��ϵͳ���洴����һ���û�������Ȩ�ޣ����ڲ���;�൱�ڸ��û�root user
		
		auth
			.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")// �����������ݿ������ˣ��ǲ����Ѿ�ע����û����ǲ���enabled;
			.authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");//��仰������authority�û������ң�������û��ǲ�����Ȩ�û���
		
	}

       @SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
	
}
