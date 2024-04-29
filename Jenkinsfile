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
            sh(script: '''cd /var/lib/jenkins/workspace/jenkins-FindFounder/findFounder''')
            sh(script: '''pwd''')
            sh(script: '''sudo docker build -t my-app .''')
        }

      stage('Tag') {
              sh(script: '''sudo docker tag my-app ${DOCKER_USER_ID}/my-app:${BUILD_NUMBER}''') 
            }

      stage('Push') {
            sh(script: 'sudo docker login -u ${DOCKER_USER_ID} -p ${DOCKER_USER_PASSWORD}') 
            sh(script: 'sudo docker push ${DOCKER_USER_ID}/my-app:${BUILD_NUMBER}') 
        }
      
stage('Deploy') {
            sshagent(credentials: ['flask-ec2-server']) {
                sh(script: 'ssh -o StrictHostKeyChecking=no ubuntu@13.125.183.79 "sudo docker rm -f docker-flask"')
                sh(script: 'ssh ubuntu@13.125.183.79 "sudo docker run --name docker-flask --env-file .env -e TZ=Asia/Seoul -p 80:80 -d -t \${DOCKER_USER_ID}/my-app:\${BUILD_NUMBER}"')
        }
    }

    stage('Cleaning up') { 
              sh "sudo docker rmi ${DOCKER_USER_ID}/my-app:${BUILD_NUMBER}" // sudo docker image 제거
      } 
    }
  }
