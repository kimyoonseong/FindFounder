node { 
    withCredentials([[$class: 'UsernamePasswordMultiBinding', 
        credentialsId: 'docker-login', 
        usernameVariable: 'DOCKER_USER_ID', 
        passwordVariable: 'DOCKER_USER_PASSWORD']]) 
    
    { 
     stage('Pull') {
           git branch: 'develop', credentialsId: 'github-access-token', url: 'https://github.com/kimyoonseong/FindFounder.git'
        }
        

      stage('Build') {
            sh(script: '''yes | sudo docker image prune -a''') 
            sh(script: '''sudo cp /home/ubuntu/.env /var/lib/jenkins/workspace/jenkins-FindFounder/findFounder/.env''')
            sh(script: '''sudo cp /home/ubuntu/application.properties /var/lib/jenkins/workspace/jenkins-FindFounder/findFounder/src/main/resources/application.properties''')
            sh(script: '''sudo docker build -f /var/lib/jenkins/workspace/jenkins-FindFounder/findFounder/Dockerfile -t my-app .''')
        }

      stage('Tag') {
              sh(script: '''sudo docker tag my-app ${DOCKER_USER_ID}/my-app:${BUILD_NUMBER}''') 
            }

      stage('Push') {
            sh(script: 'sudo docker login -u ${DOCKER_USER_ID} -p ${DOCKER_USER_PASSWORD}') 
            sh(script: 'sudo docker push ${DOCKER_USER_ID}/my-app:${BUILD_NUMBER}') 
        }
      
      stage('Deploy') {
            sshagent(credentials: ['aws-ssh-pem-key']) {
                sh(script: 'ssh -o StrictHostKeyChecking=no ubuntu@15.164.184.189 "sudo docker rm -f my-app"')
                sh(script: 'ssh ubuntu@15.164.184.189 "sudo docker run --name my-app --env-file .env -e TZ=Asia/Seoul -p 80:8080 -d -t \${DOCKER_USER_ID}/my-app:\${BUILD_NUMBER}"')
        }
    }

    stage('Cleaning up') { 
              sh "sudo docker rmi ${DOCKER_USER_ID}/my-app:${BUILD_NUMBER}" // sudo docker image 제거
      } 
    }
  }
