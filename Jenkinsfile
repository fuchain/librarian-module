pipeline {
    agent any

    stages {
        stage('init') {
            steps {
                script {
                    def scmVars = checkout scm
                    env.MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT = scmVars.GIT_PREVIOUS_SUCCESSFUL_COMMIT

                    echo "Previous git commit: ${env.MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT}"
                }
            }
        }
        stage('build-webapp-staging') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^SourceCode/WebServer'")
                    return !matches
                }
            }
            steps {
                build 'webapp-staging'
            }
        }
        stage('build-webserver-staging') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^SourceCode/WebApp'")
                    return !matches
                }
            }
            steps {
                build 'webserver-staging'
            }
        }
    }
}