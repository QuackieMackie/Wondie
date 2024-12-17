plugins {
    application
    id( "com.gradleup.shadow") version "8.3.1"
}

application.mainClass = "net.pathos.pathosbot.PathosBot"
group = "net.pathoscraft.pathosbot"
version = "1.0.0"

val JDA_VERSION = "5.2.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:$JDA_VERSION")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation("io.github.cdimascio:java-dotenv:5.2.2")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    sourceCompatibility = "1.8"
}
