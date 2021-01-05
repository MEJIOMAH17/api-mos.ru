plugins {
    java
    kotlin("jvm") version "1.4.0"
    `maven-publish`
}

group = "com.github"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.rybalkinsd:kohttp:0+")
    implementation("commons-lang:commons-lang:2+")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.+")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5+")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5+") // for kotest framework
    testImplementation("io.kotest:kotest-runner-junit5:4+") // for kotest framework
    testImplementation("io.kotest:kotest-assertions-core:4+") // for kotest core jvm assertions
}

val githubToken: String by project
val githubUser: String by project

tasks.withType<Test> {
    useJUnitPlatform()
}


publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/MEJIOMAH17/api-mos.ru")
            credentials {
                username = githubUser
                password = githubToken
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}