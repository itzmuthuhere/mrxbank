pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    environment {
        VERSION = 'v1'
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/itzmuthuhere/mrxbank.git'
            }
        }

        stage('Detect Changes') {
            steps {
                script {
                    def commitCount = sh(
                        script: 'git rev-list --count HEAD',
                        returnStdout: true
                    ).trim().toInteger()

                    if (commitCount < 2) {
                        echo "Initial run or shallow clone – building all services"
                        changedDirs = ['accounts', 'loans', 'cards']
                    } else {
                        def diff = sh(
                            script: 'git diff --name-only HEAD~1 HEAD || echo ""',
                            returnStdout: true
                        ).trim()

                        changedDirs = diff ? diff.split("\n").collect { it.split("/")[0] }.unique() : []
                    }

                    echo "Changed services: ${changedDirs}"
                }
            }
        }

        stage('Build with Jib') {
            when {
                expression { return changedDirs && changedDirs.size() > 0 }
            }
            steps {
                script {
                    changedDirs.each { service ->
                        if (['accounts', 'cards', 'loans'].contains(service)) {
                            dir(service) {
                                sh "mvn compile jib:dockerBuild -DskipTests"
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        success {
            echo '✅ All services built and deployed!'
        }
        failure {
            echo '❌ Build or deployment failed.'
        }
    }
}
