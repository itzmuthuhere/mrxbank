pipeline {
    agent any

    tools {
        maven 'maven3'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Jib') {
            steps {
                echo 'Building local Docker images using Jib...'
                dir('accounts') {
                    bat '.\\mvnw.cmd compile jib:dockerBuild -DskipTests'
                }
                dir('cards') {
                    bat '.\\mvnw.cmd compile jib:dockerBuild -DskipTests'
                }
                dir('loans') {
                    bat '.\\mvnw.cmd compile jib:dockerBuild -DskipTests'
                }
            }
        }

        stage('List Docker Images') {
            steps {
                bat 'docker images'
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
