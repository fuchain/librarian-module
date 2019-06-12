pipeline {
    agent any

    stages {
        stage('Initial environment') {
            steps {
                script {
                    def scmVars = checkout scm
                    env.MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT = scmVars.GIT_PREVIOUS_SUCCESSFUL_COMMIT
                }
            }
        }
        stage('Build WebApp') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^SourceCode/WebServer'")
                    return !matches
                }
            }
            steps {
                build 'webserver-staging'
            }
        }
        stage('Build Java') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^SourceCode/WebApp'")
                    return !matches
                }
            }
            steps {
                build 'webapp-staging'
            }
        }
        stage('Build Node') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^SourceCode/NodeServer'")
                    return !matches
                }
            }
            steps {
                build 'nodeserver-staging'
            }
        }
    }
}