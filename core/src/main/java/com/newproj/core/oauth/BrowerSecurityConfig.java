package com.newproj.core.oauth;


//@Configuration
public class BrowerSecurityConfig {/*extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()                    		//  定义当需要用户登录时候，转到的登录页面。
                .loginPage("/login.html")           //定义登陆页面
                .loginProcessingUrl("/user/login")  // 定义登陆接口
        		.and()
                .authorizeRequests()  // 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/login.html").permitAll()  //设置所有人都可以访问
                .anyRequest()               // 任何请求,登录后可以访问
                .authenticated()
                .and()
                .csrf().disable();  // 关闭csrf防护
    }*/
}