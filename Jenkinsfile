pipeline {
  agent any
  stages {
    stage('Checkout code') {
      steps {
        git(url: 'https://github.com/FahrihFatahilah/minio-spring', branch: 'dev', changelog: true)
      }
    }

  }
}