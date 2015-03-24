CLAY (Common LAYer module)
=============

[![release](http://github-release-version.herokuapp.com/github/camelot-framework/clay/release.svg?style=flat)](https://github.com/camelot-framework/clay/releases/latest)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.yandex.qatools.clay/clay/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/ru.yandex.qatools.clay/clay) 
[![covarage](https://img.shields.io/sonar/http/sonar.qatools.ru/ru.yandex.qatools.clay:clay/coverage.svg?style=flat)](http://sonar.qatools.ru/dashboard/index/569)

## Clay Maven Settings Builder

Fluent-api builders for maven settings.

```java
Settings settings = newSettings()
        .withActiveProfile(newProfile()
                .withId("profile")
                .withRepository(newRepository()
                        .withUrl("http://repo1.maven.org/maven2"))).build();
```

## Clay Aether

Fluent-api for Eclipse Aether. Easy to resolve/collect/install/deploy artifacts.

```java
//resolve with transitives 
List<ArtifactResult> results = aether(localRepositoryFile, settings)
        .resolve("ru.yandex.qatools.allure:allure-model:jar:1.3.9").get();
}

//resolve without transitives
List<ArtifactResult> results = aether(localRepositoryFile, settings)
        .resolve("ru.yandex.qatools.allure:allure-model:jar:1.3.9", false);
        
//resolve all given artifacts 
List<ArtifactResult> results = aether(localRepo, MAVEN_SETTINGS)
        .resolveAll("ru.yandex.qatools.allure:allure-model:jar:1.3.9", 
                    "ru.yandex.qatools.allure:allure-java-annotations:jar:1.3.9").get();
        
//collect
List<Artifact> artifacts = aether(localRepositoryFile, settings)
        .collect("ru.yandex.qatools.allure:allure-model:jar:1.3.9");

//install
aether(localRepositoryFile, settings)
        .install(archiveToDeploy, "testGroupId", "testArtifactId", "testVersion");

//deploy
aether(localRepositoryFile, settings)
        .deploy(distributionManagement, archiveToDeploy, "testGroupId", "testArtifactId", "testVersion");


```

Also you have few ways to get resolve results:

```java
//as list of artifact results:
List<ArtifactResult> results = aether(localRepositoryFile, settings)
        .resolve("ru.yandex.qatools.allure:allure-model:jar:1.3.9").get();
        
//as array of urls:
URL[] urls = aether(localRepositoryFile, settings)
        .resolve("ru.yandex.qatools.allure:allure-model:jar:1.3.9").getAsUrls();
        
//as class path:
String[] cp = aether(localRepositoryFile, settings)
        .resolve("ru.yandex.qatools.allure:allure-model:jar:1.3.9").getAsClassPath();
        
//as ClassLoader:
ClassLoader cl = aether(localRepositoryFile, settings)
        .resolve("ru.yandex.qatools.allure:allure-model:jar:1.3.9").getAsClassLoader();
```

## Clay Utils

Some useful tools, such as **ArchiveUtil** (configurable unpack jar's), **DateUtil** and **MapUtil**.
