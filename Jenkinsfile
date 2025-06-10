pipeline {
    agent any

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning code...'
                checkout scm
            }
        }

        stage('Build with Jib') {
            steps {
                echo 'Building Docker images using Jib...'
                sh './mvnw clean compile jib:dockerBuild -DskipTests'
            }
        }

        stage('Logs') {
            steps {
                echo 'Listing target directory...'
                sh 'ls -l'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed.'
        }
    }
}
