pipeline {
    agent any

    environment {
        REMOTE_HOST = '	10.10.1.6'
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
                // SSH Credentials를 환경변수로 바인딩
                withCredentials([sshUserPrivateKey(credentialsId: 'backend-ssh', keyFileVariable: 'SSH_KEY', usernameVariable: 'SSH_USER')]) {
                    sh """
                    scp -i \$SSH_KEY -o StrictHostKeyChecking=no build/libs/*.jar \$SSH_USER@${REMOTE_HOST}:${REMOTE_PATH}/${JAR_NAME}
                    ssh -i \$SSH_KEY -o StrictHostKeyChecking=no \$SSH_USER@${REMOTE_HOST} 'sudo systemctl restart tour-backend'
                    """
                }
            }
        }
    }
}
