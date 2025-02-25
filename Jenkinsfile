pipeline {
    agent any

    tools {
        jdk ("jdk21")
    }

    stages {
        stage('Prepare') {
            steps {
                git branch: 'master',
                    url: 'git@github.com:Team-Qampus/Quampus_BE.git'
            }
            post {
                success { 
                    sh 'echo "Successfully Cloned Repository"'
                }
                failure {
                    sh 'echo "Failed to Clone Repository"'
                }
            }    
        }

        stage('Build Gradle Test') {
            steps {
                sh 'echo "Build Gradle Test Start"'
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
            }
            post {
                success { 
                    sh 'echo "Successfully Built Gradle Project"'
                }
                failure {
                    sh 'echo "Failed to Build Gradle Project"'
                }
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
                success { 
                    sh 'echo "Successfully Removed Docker Containers"'
                }
                failure {
                    sh 'echo "Failed to Remove Docker Containers"'
                }
            }
        }

        stage('Build Docker Image with Docker Compose') {
            steps {
                sh 'echo "Building Docker Image with Docker Compose"'
                sh 'docker compose up -d --build'
            }
            post {
                success {
                    sh 'echo "Successfully Built and Started Containers with Docker Compose"'
                }
                failure {
                    sh 'echo "Failed to Build Docker Image"'
                }
            }
        }
    }
}
