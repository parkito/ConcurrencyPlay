dependencies {
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.6.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.2")
    testImplementation("io.projectreactor:reactor-test:3.4.13")
}