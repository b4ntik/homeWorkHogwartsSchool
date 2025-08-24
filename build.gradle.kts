plugins {
    id("java")
    id("org.springframework.boot") version "3.5.4" // Указываем версию Spring Boot
    id("io.spring.dependency-management") version "1.1.0"
}

group = "ru.hogwarts"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone")}
}


dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-jdbc")
    implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.test {
    useJUnitPlatform()
}