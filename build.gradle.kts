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
    testImplementation("org.assertj:assertj-core:3.27.6")

    implementation("org.openjdk.jmh:jmh-core:1.37")
    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")

    implementation("it.unimi.dsi:fastutil:8.5.13")
}

jmh {
    includes.set(listOf("com.itmo.Benchmarks.*"))
}

tasks.test {
    useJUnitPlatform()
}