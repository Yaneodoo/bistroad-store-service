package kr.bistroad.storeservice

import com.fasterxml.classmate.TypeResolver
import io.swagger.annotations.ApiModelProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.any
import springfox.documentation.builders.RequestHandlerSelectors.basePackage
import springfox.documentation.schema.AlternateTypeRules.newRule
import springfox.documentation.service.Tag
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig(
    private val typeResolver: TypeResolver
) {
    @Bean
    fun swaggerDocket() = Docket(DocumentationType.OAS_30)
        .apiInfo(
            ApiInfoBuilder().title("Store API").build()
        )
        .tags(Tag("/stores", "Store API"))
        .tags(Tag("/stores/**/items", "Store Item API"))
        .tags(Tag("/stores/**/items/**/upload-photo", "Store Item Photo Upload API"))
        .alternateTypeRules(
            newRule(
                typeResolver.resolve(Pageable::class.java),
                typeResolver.resolve(Page::class.java)
            )
        )
        .select()
        .apis(basePackage("org.springframework.boot").negate())
        .paths(any())
        .build()!!

    data class Page(
        @ApiModelProperty("\${swagger.doc.model.page.page.description}")
        val page: Int,

        @ApiModelProperty("\${swagger.doc.model.page.size.description}")
        val size: Int,

        @ApiModelProperty("\${swagger.doc.model.page.sort.description}")
        val sort: List<String>
    )
}