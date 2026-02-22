#!/usr/bin/env groovy
package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String imageName) {
        script.echo "building the docker image... through sharedlib"
        script.sh "podman-remote build --build-arg HTTP_PROXY=${script.env.HTTP_PROXY} --build-arg HTTPS_PROXY=${script.env.HTTPS_PROXY} --build-arg NO_PROXY=${script.env.NO_PROXY} -t $imageName ."
        }

    def dockerLogin() {
        scritp.sh 'Dcoker login'
        script.withCredentials([script.usernamePassword(credentialsId: 'docker-hub-repo', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.sh "echo '${script.PASS}' | podman-remote login -u '${script.USER}' --password-stdin"
        }
    }

    def dockerPush(String imageName) {
        script.sh 'Pushing image to docker repo from sharedlib'
        def newImage= "docker.io/sushantsaini29/${imageName}"
        script.sh "podman-remote tag localhost/${imageName} ${newImage}"
        script.sh "podman-remote push ${newImage} --tls-verify=false"
    }
}