pipeline {
    agent any

    
    tools {
        jdk ("jdk21")
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
                sh './gradlew clean build  -x test --info'
            }
            post {
                success { sh 'echo "Successfully Built Gradle Project"' }
                failure { sh 'echo "Failed to Build Gradle Project"' }
            }
        }

        stage('Check and Free Up Ports') {
            steps {
                sh 'echo "Checking and Freeing Up Ports (6380 for Redis)"'
                sh """
                if lsof -i :6380; then
                    echo "Port 6380 is in use. Killing the process..."
                    sudo kill -9 \$(lsof -ti :6380)
                else
                    echo "Port 6380 is free."
                fi
                """
            }
        }

        stage('Remove Existing Docker Containers') {
            steps {
                    sh 'echo "Stopping and Removing Existing Docker Containers"'
                    sh 'docker ps -a' // 현재 실행 중인 컨테이너 확인
                    sh 'docker stop $(docker ps -aq) || true'
                    sh 'docker rm -f $(docker ps -aq) || true'
                    sh 'docker-compose down --rmi all --volumes --remove-orphans || true'
            }
            post {
                success { sh 'echo "Successfully Removed Docker Containers"' }
                failure { sh 'echo "Failed to Remove Docker Containers"' }
            }
        }

        stage('Build and Deploy with Docker Compose') {
            steps {
                  sh 'echo "Building and Deploying Containers with Docker Compose"'
                  sh 'docker system prune -af'
                  sh 'docker-compose up -d --build'
            }
            post {
                success { sh 'echo "Successfully Built and Started Containers"' }
                failure { sh 'echo "Failed to Deploy Containers"' }
            }
        }
    }
}
