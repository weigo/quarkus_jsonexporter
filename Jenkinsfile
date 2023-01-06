#!/usr/bin/env groovy

def version = "0.0.1-${BUILD_NUMBER}"
def branchName = "${env.BRANCH_NAME}".replace('/', '-').toLowerCase()
def imageBaseName = "weigo/quarkus_json_exporter"
def imageName = "${imageBaseName}:${branchName}-${version}"
def dockerPlatforms = "linux/amd64,linux/arm64"

pipeline {
    agent any

    options { timestamps() }

    tools {
        jdk 'openjdk-17'
        maven 'maven-3.8'
    }

    parameters {
        string(name: "DOCKER_REGISTRY", defaultValue: "nexus.weigo.org:18080", trim: true, description: "Docker pull registry url")
        string(name: "DOCKER_PUSH_REGISTRY", defaultValue: "nexus.weigo.org:18081", trim: true, description: "Docker push registry url")
        string(name: "DOCKER_REGISTRY_PULL_USER", defaultValue: "docker_pull_user", trim: true, description: "Docker registry pull user")
        string(name: "DOCKER_REGISTRY_PUSH_USER", defaultValue: "docker_push_user", trim: true, description: "Docker registry push user")
        string(name: "MAVEN_SETTINGS", defaultValue: "maven-settings", trim: true, description: "ID of global Maven settings.xml")
    }

    stages {
        stage('prepare') {
            when {
                branch 'master'
            }
            steps {
                script {
                    imageName = "${imageBaseName}:${version}"
                }
            }
        }

        stage('build app') {
            steps {
                withMaven(mavenSettingsConfig: "${params.MAVEN_SETTINGS}") {
                    sh 'mvn clean package'
                }
            }
        }

        stage('build docker') {
            steps {
                script {
                    docker.withRegistry("https://${params.DOCKER_REGISTRY}", "${params.DOCKER_REGISTRY_PULL_USER}") {
                        sh """
                        docker buildx create --use --name multiarch
                        docker buildx build --build-arg DOCKER_REGISTRY=${params.DOCKER_REGISTRY} \
                        --platform ${dockerPlatforms} -f src/main/docker/Dockerfile.jvm -t ${params.DOCKER_PUSH_REGISTRY}/${imageName} .
                           """
                    }
                    docker.withRegistry("https://${params.DOCKER_PUSH_REGISTRY}", "${params.DOCKER_REGISTRY_PUSH_USER}") {
                        sh """
                        docker buildx create --use --name multiarch
                        docker buildx build --push --build-arg DOCKER_REGISTRY=${params.DOCKER_REGISTRY} \
                        --platform ${dockerPlatforms} -f src/main/docker/Dockerfile.jvm -t ${params.DOCKER_PUSH_REGISTRY}/${imageName} .
                           """
                    }
                }
            }
        }
    }
}
