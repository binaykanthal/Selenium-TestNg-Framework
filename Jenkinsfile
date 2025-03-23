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
            script {
                def buildStatus = currentBuild.currentResult
                def projectName = env.JOB_NAME
                def buildNumber = env.BUILD_NUMBER
                def buildUrl = env.BUILD_URL
                def gitCommit = env.GIT_COMMIT
                def gitBranch = env.GIT_BRANCH
                def reportUrl = "http://localhost:8080/job/OrangeHRM_Build/HTML_20Extent_20Report/"
			success{
                emailext (
					to: 'binaykanthal2995@gmail.com'
                    subject: "Jenkins Build Success - ${projectName} #${buildNumber}",
                    body: """
                        Hello Team,<br><br>
                        The latest Jenkins build has completed.<br><br>
                        🛠️ <b>Project Name</b>: ${projectName} <br>
                        🔢 <b>Build Number</b>: ${buildNumber} <br>
                        📊 <b>Build Status</b>: <span style="color:green;"><b>${buildStatus}</b></span> <br>
                        🔗 <b>Build URL</b>: <a href="${buildUrl}">Click here</a> <br><br>
                        📝 <b>Last Commit</b>: ${gitCommit} <br>
                        📂 <b>Branch</b>: ${gitBranch} <br><br>
                        📌 <b>Build log is attached.</b><br><br>
                        📑 <b>Extent Report </b>: <a href="${reportUrl}">Click here</a><br><br>
                        Best regards,<br>
                        <b>Automation Team</b>
                    """,
                    mimeType: 'text/html',
                   	attachLog:true
                )
               }
               failure{
				emailext (
					to: 'binaykanthal2995@gmail.com'
                    subject: "Jenkins Build Success - ${projectName} #${buildNumber}",
                    body: """
                        Hello Team,<br><br>
                        The latest Jenkins build has completed.<br><br>
                        🛠️ <b>Project Name</b>: ${projectName} <br>
                        🔢 <b>Build Number</b>: ${buildNumber} <br>
                        📊 <b>Build Status</b>: <span style="color:red;"><b>${buildStatus}</b></span> <br>
                        🔗 <b>Build URL</b>: <a href="${buildUrl}">Click here</a> <br><br>
                        📝 <b>Last Commit</b>: ${gitCommit} <br>
                        📂 <b>Branch</b>: ${gitBranch} <br><br>
                        📌 <b>Build log is attached.</b><br><br>
                        <b>Please check the logs and take necessary actions.</b><br><br>
                        📑 <b>Extent Report (if available)</b>: <a href="${reportUrl}">Click here</a><br><br>
                        Best regards,<br>
                        <b>Automation Team</b>
                    """,
                    mimeType: 'text/html',
                   	attachLog:true
                )
			   }
            }
        }
    }
