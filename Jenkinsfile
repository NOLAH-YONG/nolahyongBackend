pipeline {
    agent any

    environment {
        REMOTE_HOST = '10.10.1.6'  // 공백/탭 제거
        REMOTE_PATH = '/home/ubuntu/backend'
        JAR_NAME = 'app.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'tour_admin',
                    branch: 'main',
                    url: 'https://github.com/NOLAH-YONG/nolahyongBackend.git'
            }
        }

        stage('Test') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew test'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build'
            }
        }

        stage('Deploy') {
            steps {
                sshagent(credentials: ['backend-ssh']) {
                    sh """
                    scp -P 2222 -o StrictHostKeyChecking=no build/libs/*[!plain].jar ubuntu@${REMOTE_HOST}:${REMOTE_PATH}/${JAR_NAME}
                    ssh -p 2222 -o StrictHostKeyChecking=no ubuntu@${REMOTE_HOST} 'sudo systemctl restart tour-backend'
                    """
                }
            }
        }
    }
}
