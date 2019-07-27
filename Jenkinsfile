pipeline {
    agent any

    stages {
        stage('Initial') {
            steps {
                script {
                    def scmVars = checkout scm
                    env.MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT = scmVars.GIT_PREVIOUS_SUCCESSFUL_COMMIT
                }
            }
        }
        stage('Main Server') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^bcserver'")
                    return !matches
                }
            }
            steps {
                build 'bcserver-staging'
            }
        }
        stage('Secondary Server') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^ioserver'")
                    return !matches
                }
            }
            steps {
                build 'ioserver-staging'
            }
        }
        stage('Web App') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^webapp'")
                    return !matches
                }
            }
            steps {
                build 'webapp-staging'
            }
        }
        stage('Visualize App') {
            when {
                expression {
                    matches = sh(returnStatus: true, script: "git diff --name-only $MY_GIT_PREVIOUS_SUCCESSFUL_COMMIT|egrep -q '^visualizeapp'")
                    return !matches
                }
            }
            steps {
                build 'visualizeapp-staging'
            }
        }
    }
}