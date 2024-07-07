plugins {
    java
    kotlin("jvm") version "1.9.22"
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-validation")
    implementation("com.google.transit:gtfs-realtime-bindings:0.0.4")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jeasy:easy-random-core:5.0.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
