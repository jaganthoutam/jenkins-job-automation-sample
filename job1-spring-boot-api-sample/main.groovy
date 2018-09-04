String basePath = 'springboot-api-demo'
String repo = 'anuprasanna/springboot-api-demo'

folder(basePath) {
    displayName('Springboot API Demo')
    description 'This example shows basic folder/job creation.'
}


job(basePath + '/Build') {
    scm {
      github repo  
    }
    triggers {
        scm('H/15 * * * *')
    }
    steps {
        maven('-e clean install')
    }
}