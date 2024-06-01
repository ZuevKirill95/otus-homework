plugins {
    id("java")
}

group = "ru.otus"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic")
    implementation("org.apache.commons:commons-lang3")
    compileOnly("org.projectlombok:lombok")
}

tasks.test {
    useJUnitPlatform()
}