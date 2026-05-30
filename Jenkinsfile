#!/usr/bin/env groovy

def version = "0.0.1-${BUILD_NUMBER}"
def branchName = "${env.BRANCH_NAME}".replace('/', '-').toLowerCase()
def imageBaseName = "weigo/quarkus_json_exporter"
def imageName = "${imageBaseName}:${branchName}-${version}"
def latestImageName = "${imageBaseName}:${branchName}-latest"

pipeline {
    agent any

    options { timestamps() }

    parameters {
        string(name: "DOCKER_REGISTRY", defaultValue: "nexus.weigo.org:18080", trim: true, description: "Docker pull registry url")
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
                    latestImageName = "${imageBaseName}:latest"
                }
            }
        }

        stage('docker image') {
            steps {
                script {
                    configFileProvider([configFile(fileId: "${params.MAVEN_SETTINGS}", variable: 'SETTINGS_XML')]) {
                        sh """
                           cp $SETTINGS_XML settings.xml
                           docker build --build-arg DOCKER_REGISTRY="${params.DOCKER_REGISTRY}/" -f src/main/docker/Dockerfile.jvm 
--tag="${params.DOCKER_REGISTRY}/${imageName}" .
                           docker push "${params.DOCKER_REGISTRY}/${imageName}"
                           docker tag "${params.DOCKER_REGISTRY}/${imageName}" "${params.DOCKER_REGISTRY}/${latestImageName}"
                           docker push "${params.DOCKER_REGISTRY}/${latestImageName}"
                       """
                    }
                }
            }
        }
    }
}
