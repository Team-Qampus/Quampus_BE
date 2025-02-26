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
        sh 'echo "Stopping and Removing Existing Docker Containers except Jenkins"'
        sh '''
        JENKINS_CONTAINER=$(docker ps -aqf "name=jenkins")
        
        # 모든 컨테이너 ID 가져오기
        ALL_CONTAINERS=$(docker ps -aq)

        # Jenkins 컨테이너 제외하고 정지 및 삭제
        for CONTAINER in $ALL_CONTAINERS; do
            if [ "$CONTAINER" != "$JENKINS_CONTAINER" ]; then
                docker stop $CONTAINER || true
                docker rm -f $CONTAINER || true
            fi
        done
        
        # Docker-compose 사용하여 제거 (Jenkins 영향 없음)
        docker-compose down --rmi all --volumes --remove-orphans || true
        '''
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
