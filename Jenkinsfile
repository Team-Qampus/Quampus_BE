pipeline {
    agent any

    environment {
        JAVA_HOME = "/usr/lib/jvm/temurin-21-jdk-amd64"  
        PATH = "${JAVA_HOME}/bin:${PATH}"
    }
    
    tools {
        jdk 'jdk21'
    }

    stages {
        stage('Prepare') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/Team-Qampus/Quampus_BE.git'
            }
            post {
                success { sh 'echo "Successfully Cloned Repository"' }
                failure { sh 'echo "Failed to Clone Repository"' }
            }
        }

        stage('Build Gradle Test') {
            steps {
                sh 'echo "Build Gradle Test Start"'
                sh 'echo "JAVA_HOME is set to: $JAVA_HOME"'
                sh 'java -version'
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
            }
            post {
                success { sh 'echo "Successfully Built Gradle Project"' }
                failure { sh 'echo "Failed to Build Gradle Project"' }
            }
        }

        stage('Remove Existing Docker Containers') {
            steps {
                sh 'echo "Stopping and Removing Existing Docker Containers"'
                sh """
                docker stop qampus-be || true
                docker rm -f qampus-be || true
                docker rmi -f qampus-be || true
                """
            }
            post {
                success { sh 'echo "Successfully Removed Docker Containers"' }
                failure { sh 'echo "Failed to Remove Docker Containers"' }
            }
        }

        stage('Build and Deploy with Docker Compose') {
            steps {
                sh 'echo "Building and Deploying Containers with Docker Compose"'
                sh 'docker compose up -d --build'
            }
            post {
                success { sh 'echo "Successfully Built and Started Containers"' }
                failure { sh 'echo "Failed to Deploy Containers"' }
            }
        }
    }
}
