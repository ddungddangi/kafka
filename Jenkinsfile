pipeline {
    agent any

    parameters {
        string(name: 'VERSION', defaultValue: 'v0.2', description: '도커 이미지 태그')
    }

    environment {
        BACKEND_IMAGE = "gaemineunttungttung/backend:${params.VERSION}"
        FRONTEND_IMAGE = "gaemineunttungttung/frontend:${params.VERSION}"
    }

    stages {
        stage('Git Clone') {
            steps {
                git branch: 'main', credentialsId: 'github-credentials', url: 'https://github.com/ddungddangi/kafka.git'
            }
        }

        stage('Build Backend Image') {
            steps {
                dir('backend') {
                    sh '''
                    docker build -t $BACKEND_IMAGE .
                    '''
                }
            }
        }

        stage('Push Backend Image') {
            steps {
                withDockerRegistry(credentialsId: 'docker-hub-credentials', url: '') {
                    sh 'docker push $BACKEND_IMAGE'
                }
            }
        }

        stage('Build Frontend Image') {
            steps {
                dir('frontend') {
                    sh '''
                    docker build -t $FRONTEND_IMAGE .
                    '''
                }
            }
        }

        stage('Push Frontend Image') {
            steps {
                withDockerRegistry(credentialsId: 'docker-hub-credentials', url: '') {
                    sh 'docker push $FRONTEND_IMAGE'
                }
            }
        }
    }
}

