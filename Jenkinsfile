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

        stage('build docker image') {
            steps {
                script {
                    configFileProvider([configFile(fileId: '8cf374e4-afaa-4861-9153-87b0ee09d83c', variable: 'SETTINGS_XML')]) {
                    sh """
                       cp $SETTINGS_XML settings.xml
                       docker build --build-arg DOCKER_REGISTRY="${params.DOCKER_REGISTRY}/" -f src/main/docker/Dockerfile.jvm \\ 
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

    post {
        success {
            emailext(body: '${SCRIPT, template = "groovy-html.template"}',
                    mimeType: 'text/html',
                    from: 'jenkins',
                    recipientProviders: [
                            [$class: 'DevelopersRecipientProvider'],
                            [$class: 'RequesterRecipientProvider']
                    ],
                    subject: "[JENKINS] '${JOB_NAME}' (${BUILD_NUMBER}) abgeschlossen - Image ${imageName} gebaut.")
        }
        failure {
            emailext(body: '${SCRIPT, template = "groovy-html.template"}',
                    mimeType: 'text/html',
                    from: 'jenkins',
                    recipientProviders: [
                            [$class: 'DevelopersRecipientProvider'],
                            [$class: 'RequesterRecipientProvider']
                    ],
                    subject: "[JENKINS] FAILURE: '${JOB_NAME}' (${BUILD_NUMBER}) wegen Fehlern abgebrochen.")
        }
    }
}
