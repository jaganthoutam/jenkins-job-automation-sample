// Jenkins Job DSL definition file for generating a Job for 
// https://github.com/anuradhaneo/springboot-api-demo

String basePath = 'springboot-api-demo'
String gitRepository = 'anuradhaneo/springboot-api-demo'
String buildBranch = '*/master'
String projectName = 'Build Project'
String projectDisplayName = 'Springboot API Sample'
String projectDescription = 'This example shows basic folder/job creation'
String credentialIDGithub = 'github-anuradhaneo'

String artifactGroupID = 'org.spring.boot.sample'
String artifactID = 'SpringBootRestApiExample'

final String STATUS_SUCCESS = 'SUCCESS'
final String HTTP = 'http'
final String HTTPS = 'https'

String nexusOSSURI = 'nexus:8081/nexus'
String nexusOSSVersion = 'nexus3'
String nexusOSSRepositoryName = 'SpringBootRestApiProject'
String nexusCredentialsID = 'adopnexus'

// root folder creation
folder(basePath) {
    displayName(projectDisplayName)
    description(projectDescription)
}

// job definition
mavenJob(basePath + '/' + projectName) {
    description('Build the Java roject: ' + gitRepository)
    scm {
        git {
            branch(buildBranch)
            remote {
                github (gitRepository, HTTPS, 'github.com')
                credentials(credentialIDGithub)
            }
        }
    }
    triggers {
        scm('@daily')
    }
    wrappers {
        goals('clean install')
        timeout {
            likelyStuck()
            failBuild()
        }
        testInProgressBuildWrapper()
    }
    postBuildSteps(STATUS_SUCCESS) {
        shell("echo 'Maven build completed !'")
        // upload the generated artifact to Nexus OSS repo
        nexusArtifactUploader {
            nexusVersion(nexusOSSVersion)
            protocol(HTTP)
            nexusUrl(nexusOSSURI)
            groupId(artifactGroupID)
            version('${POM_VERSION}')
            repository(nexusOSSRepositoryName)
            credentialsId(nexusCredentialsID)
            artifact {
                artifactId(artifactID)
                type('jar')
                classifier('')
                file('target/' + artifactID + '-${POM_VERSION}.jar')
            }
        }
    }
}
