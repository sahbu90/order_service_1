pipeline {
    agent any

    environment {
        EC2_USER = 'ubuntu'
        EC2_HOST = '51.20.153.110'
        WAR_NAME = 'order_service.war'
        REMOTE_TOMCAT_WEBAPPS = '/opt/tomcat/webapps' // adjust if different
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/sahbu90/order_service_1.git'
            }
        }

        stage('Build WAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Deploy WAR to Tomcat') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh """
                        scp target/${WAR_NAME} ${EC2_USER}@${EC2_HOST}:${REMOTE_TOMCAT_WEBAPPS}/
                        ssh ${EC2_USER}@${EC2_HOST} '
                            sudo systemctl restart tomcat || \
                            sudo /opt/tomcat/bin/shutdown.sh && sudo /opt/tomcat/bin/startup.sh
                        '
                    """
                }
            }
        }
    }
}
