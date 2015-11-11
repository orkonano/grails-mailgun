// configuration for plugin testing - will not be included in the plugin zip

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}


mailgun{
       apiKey = 'key-731feb2a2d7bcc7ad7b9a94b78734439'
       domain = 'sandbox8321393f775d459ba8d8449a976f8198.mailgun.org'

       message{
              defaultFrom = 'BigHamlet Test <postmaster@sandbox8321393f775d459ba8d8449a976f8198.mailgun.org>'
              defaultTo = 'devbamboo@gmail.com, marianoekfuri@gmail.com'
              defaultSubject = 'BigHamlet tiene promociones para vos'
              format = 'html'
              defaulTemplate = 'mailgunTest'
              defaultReplyTo = 'devbamboo@gmail.com'
       }

       tracking{
              enabled = 'yes'
              clicks{
                     enabled = 'yes'
              }
              opens{
                     enabled = 'yes'
              }
       }
}