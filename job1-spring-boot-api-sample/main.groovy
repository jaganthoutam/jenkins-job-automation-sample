String basePath = 'springboot-api-demo'
String gitRepository = 'anuprasanna/springboot-api-demo'
String buildBranch = '*/master'
String projectName = 'BuildProject'
String projectDisplayName = 'Springboot API Sample'
String projectDescription = 'This example shows basic folder/job creation'
String credentialIDGithub = 'github-anuprasanna'

String artifactGroupID = 'org.spring.boot.sample'
String artifactID = 'SpringBootRestApiExample'

String STATUS_SUCCESS = 'SUCCESS'
String HTTP = 'http'
String HTTPS = 'https'

String nexusOSSURI = 'localhost:8081'
String nexusOSSVersion = 'nexus3'
String nexusOSSRepositoryName = 'SpringBootRestApiProject'
String nexusCredentialsID = 'NexusRepoCredentials'

folder(basePath) {
    displayName(projectDisplayName)
    description(projectDescription)
}

// job definition
mavenJob(basePath + '/' + projectName) {
    description('build the project: ' + gitRepository)
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