pipeline {
    agent any

    environment {
        EC2_USER = 'ubuntu'
        EC2_HOST = '51.20.153.110'
        IMAGE_NAME = 'order_service_1'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sahbu90/order_service_1.git'
            }
        }

        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:latest .'
            }
        }

        stage('Save & Transfer Docker Image to EC2') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh """
                    docker save ${IMAGE_NAME}:latest | bzip2 | pv | ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} 'bunzip2 | docker load'
                    """
                }
            }
        }

        stage('Run Docker Container on EC2') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh """
                    ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                        docker stop order_service_1 || true &&
                        docker rm order_service_1 || true &&
                        docker run -d --name order_service_1 -p 8080:8080 ${IMAGE_NAME}:latest
                    '
                    """
                }
            }
        }
    }
}
