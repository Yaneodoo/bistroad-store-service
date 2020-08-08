package kr.bistroad.storeservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.ribbon.RibbonClient

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClient(name = "store-service")
class BistroadStoreServiceApplication

fun main(args: Array<String>) {
    runApplication<BistroadStoreServiceApplication>(*args)
}
