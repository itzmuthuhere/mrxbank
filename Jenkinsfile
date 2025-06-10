pipeline {
    agent any

    tools {
        maven 'Maven3'
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
                    sh './mvnw compile jib:dockerBuild -DskipTests'
                }
                dir('cards') {
                    sh './mvnw compile jib:dockerBuild -DskipTests'
                }
                dir('loans') {
                    sh './mvnw compile jib:dockerBuild -DskipTests'
                }
            }
        }

        stage('List Docker Images') {
            steps {
                sh 'docker images'
            }
        }
    }

    post {
        always {
            echo 'Pipeline execution completed.'
        }
    }
}
