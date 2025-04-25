pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'gaemineunttungttung/backend:v0.2'
    }

    stages {
        stage('Clone') {
            steps {
                git credentialsId: 'github-credentials', url: 'https://github.com/ddungddangi/kafka.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE ./backend'
            }
        }

        stage('Push Docker Image') {
            steps {
                withDockerRegistry(credentialsId: 'docker-hub-credentials', url: '') {
                    sh 'docker push $DOCKER_IMAGE'
                }
            }
        }
    }
}

