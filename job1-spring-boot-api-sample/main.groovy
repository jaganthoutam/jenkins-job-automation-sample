String basePath = 'springboot-api-demo'
String repo = 'anuprasanna/springboot-api-demo'
String buildBranch = '*/master'

folder(basePath) {
    displayName('Springboot API Sample')
    description 'This example shows basic folder/job creation.'
}

// job definition
job(basePath + '/Build') {
    scm {
      github (repo, buildBranch, 'https', 'github.com')
    }
    steps {
      	maven {
            goals('clean')
            goals('install')
            mavenOpts('-Xms256m')
            mavenOpts('-Xmx512m')
            properties(skipTests: true)
            mavenInstallation('Maven3')
            providedSettings('central-mirror')
        }
    }
}