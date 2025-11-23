plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.itmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

sourceSets {
    named("jmh") {
        java.srcDir("src/main/java")
    }
}

jmh {
    includes.set(listOf("com.itmo.Benchmarks.*"))
}

tasks.test {
    useJUnitPlatform()
}