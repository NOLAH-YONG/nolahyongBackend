pipeline {
    agent any
    stages {
        stage('Prepare'){
            steps {
                git credentialsId : 'tour_admin',
                    branch : 'main',
                    url : 'https://github.com/NOLAH-YONG/nolahyongBackend.git'
            }
        }
        stage('test') {
            steps {
                echo 'test stage'
            }
        }
        stage('build') {
            steps {
                echo 'build stage'
            }
        }
    }
}