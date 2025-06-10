pipeline {
    agent any

    tools {
        maven 'Maven' // Use the Maven tool configured in Jenkins
    }

    environment {
        VERSION = 'v1'
    }

    options {
        skipStagesAfterUnstable()
        timeout(time: 10, unit: 'MINUTES')
        timestamps()
    }

    triggers {
        githubPush() // Trigger build on push from GitHub webhook
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/itzmuthuhere/mrxbank.git'
            }
        }

        stage('Detect Changed Services') {
            steps {
                script {
                    changedDirs = sh(
                        script: "git diff --name-only HEAD~1 HEAD | cut -d/ -f1 | sort -u",
                        returnStdout: true
                    ).trim().split("\n").findAll { it in ['accounts', 'cards', 'loans'] }

                    if (changedDirs.isEmpty()) {
                        currentBuild.result = 'SUCCESS'
                        echo "🟢 No relevant service changes detected. Skipping build."
                        skipBuild = true
                    } else {
                        echo "📦 Services changed: ${changedDirs.join(', ')}"
                        skipBuild = false
                    }
                }
            }
        }

        stage('Build with Jib') {
            when {
                expression { return !skipBuild }
            }
            steps {
                script {
                    changedDirs.each { service ->
                        dir(service) {
                            echo "🚧 Building $service with Jib..."
                            sh "mvn compile jib:dockerBuild -DskipTests"
                        }
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            when {
                expression { return !skipBuild }
            }
            steps {
                echo "🚀 Starting services with Docker Compose..."
                sh "docker-compose down || true"
                sh "docker-compose up -d --build"
            }
        }
    }

    post {
        always {
            echo "📜 Pipeline finished: ${currentBuild.currentResult}"
        }
        success {
            echo '✅ All services built and deployed successfully!'
        }
        failure {
            echo '❌ Pipeline failed. Check logs above.'
        }
    }
}
