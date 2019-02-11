pipeline {
	agent any
	tools { 
        maven 'maven-3.6.0' 
        jdk 'jdk8'
    }
    stages {
        stage('Build - Windows Only') {
            steps {
                bat 'mvn -V -Dmaven.test.failure.ignore clean package'
            }
            post {
                success {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
    }
}
