pipeline {
  agent any
  stages {
    stage('error') {
      steps {
        git(url: 'https://github.com/FahrihFatahilah/minio-spring', branch: 'main', changelog: true)
      }
    }

  }
}