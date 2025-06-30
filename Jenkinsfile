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

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean build -x test'  // 테스트 없이 build
            }
        }

        stage('Deploy') {
            steps {
                withCredentials([sshUserPrivateKey(
                    credentialsId: 'backend-ssh',
                    keyFileVariable: 'SSH_KEY',
                    usernameVariable: 'SSH_USER'
                )]) {
                    sh """
                    ssh -p 2222 -i \$SSH_KEY -o StrictHostKeyChecking=no \$SSH_USER@${REMOTE_HOST} 'mkdir -p ${REMOTE_PATH}'
                    scp -P 2222 -i \$SSH_KEY -o StrictHostKeyChecking=no build/libs/nolahyong-backend-0.0.1-SNAPSHOT.jar \$SSH_USER@${REMOTE_HOST}:${REMOTE_PATH}/${JAR_NAME}
                    ssh -p 2222 -i \$SSH_KEY -o StrictHostKeyChecking=no \$SSH_USER@${REMOTE_HOST} 'sudo systemctl daemon-reload'
                    ssh -p 2222 -i \$SSH_KEY -o StrictHostKeyChecking=no \$SSH_USER@${REMOTE_HOST} 'sudo systemctl restart tour-backend'
                    """
                }
            }
        }
    }
}