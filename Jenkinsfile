pipeline {
    agent any

    tools {
        maven 'maven3'
    }

    environment {
        MAVEN_OPTS = "-Dmaven.repo.local=C:\\maven-cache"
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
            options {
                timeout(time: 30, unit: 'MINUTES') // ⏱ Timeout set to 30 mins
            }
            steps {
                echo '🔧 Building Docker images using Jib...'
                dir('accounts') {
                    bat '.\\mvnw.cmd --batch-mode compile jib:dockerBuild -DskipTests'
                }
                dir('cards') {
                    bat '.\\mvnw.cmd --batch-mode compile jib:dockerBuild -DskipTests'
                }
                dir('loans') {
                    bat '.\\mvnw.cmd --batch-mode compile jib:dockerBuild -DskipTests'
                }
            }
        }

        stage('List Docker Images') {
            options {
                timeout(time: 5, unit: 'MINUTES') // ⏱ Shorter timeout here
            }
            steps {
                bat 'docker images'
            }
        }
    }

    post {
        always {
            echo '✅ Pipeline execution completed!'
        }
    }
}
