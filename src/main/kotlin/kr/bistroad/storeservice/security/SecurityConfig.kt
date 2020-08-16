package kr.bistroad.storeservice.security

import kr.bistroad.storeservice.store.StorePermissionEvaluator
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val storePermissionEvaluator: StorePermissionEvaluator
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(jwtAuthenticationProvider)
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(JwtAuthorizationFilter(authenticationManager()))
            .authorizeRequests()
            .anyRequest().permitAll()
    }

    override fun configure(web: WebSecurity) {
        val handler = DefaultWebSecurityExpressionHandler().apply {
            setPermissionEvaluator(storePermissionEvaluator)
        }
        web.expressionHandler(handler)
    }
}