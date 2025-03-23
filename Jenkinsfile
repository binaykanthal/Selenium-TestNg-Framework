pipeline {
    agent any
    
    tools{
		mavem 'maven-3.9.8'
	}

    stages {
		stage('Checkout') {
            steps {
                git branch: 'main',url: 'https://github.com/binaykanthal/Selenium-TestNg-Framework.git'
            }
        }
        
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Reports') {
            steps {
                publishHTML(target: [
					reportDir: 'src/test/resources/ExtentReport',
					reportFiles: 'ExtentReport.html',
					reportName: 'Extent Spark Report'
				])
            }
        }
    }

    post {
        always {
			archiveArtifacts artifacts:'**/src/test/resources/ExtentReport/*.html', fingerprint:true
			junit 'target/surefire-reports/*.xml'
			}
			success{
                emailext (
					to: 'binaykanthal2995@gmail.com',
                    subject: "Jenkins Build Success - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        Hello Team,<br><br>
                        The latest Jenkins build has completed.<br><br>
                        🛠️ <b>Project Name</b>: ${env.JOB_NAME} <br>
                        🔢 <b>Build Number</b>: ${env.BUILD_NUMBER} <br>
                        📊 <b>Build Status</b>: <span style="color:green;"><b>Success</b></span> <br>
                        🔗 <b>Build URL</b>: <a href="${env.BUILD_URL}">Click here</a> <br><br>
                        📝 <b>Last Commit</b>: ${env.GIT_COMMIT} <br>
                        📂 <b>Branch</b>: ${env.GIT_BRANCH} <br><br>
                        📌 <b>Build log is attached.</b><br><br>
                        📑 <b>Extent Report </b>: <a href="${http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/}">Click here</a><br><br>
                        Best regards,<br>
                        <b>Automation Team</b>
                    """,
                    mimeType: 'text/html',
                   	attachLog:true
                )
               }
               failure{
				emailext (
					to: 'binaykanthal2995@gmail.com',
                    subject: "Jenkins Build Failed - ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    body: """
                        Hello Team,<br><br>
                        The latest Jenkins build has completed.<br><br>
                        🛠️ <b>Project Name</b>: ${env.JOB_NAME} <br>
                        🔢 <b>Build Number</b>: ${env.BUILD_NUMBER} <br>
                        📊 <b>Build Status</b>: <span style="color:red;"><b>Failed</b></span> <br>
                        🔗 <b>Build URL</b>: <a href="${env.BUILD_URL}">Click here</a> <br><br>
                        📝 <b>Last Commit</b>: ${env.GIT_COMMIT} <br>
                        📂 <b>Branch</b>: ${env.GIT_BRANCH} <br><br>
                        📌 <b>Build log is attached.</b><br><br>
                        <b>Please check the logs and take necessary actions.</b><br><br>
                        📑 <b>Extent Report (if available) </b>: <a href="${http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/}">Click here</a><br><br>
                        Best regards,<br>
                        <b>Automation Team</b>
                    """,
                    mimeType: 'text/html',
                   	attachLog:true
                )
			   }
            }
        }

