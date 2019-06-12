pipeline {
    agent {
        node {
            label 'common-ws-agent'
            customWorkspace 'librarian-staging'
        }
    }

    stages {
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