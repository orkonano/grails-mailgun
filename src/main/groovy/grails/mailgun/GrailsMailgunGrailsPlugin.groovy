package grails.mailgun

import grails.plugin.mailgun.render.DefaultEmailHtmlRender
import grails.plugins.*
import grails.plugins.rest.client.RestBuilder

class GrailsMailgunGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/**",
            "grails-app/views/test/mailgunTest.gsp",
            "**/demo/**"
    ]

    def profiles = ['web']

    // TODO Fill in these fields
    def title = "Mailgun Plugin" // Headline display name of the plugin
    def author = "Mariano Kfuri"
    def authorEmail = "orquito@gmail.com"
    def description = '''\
Brief summary/description of the plugin.
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/orkonano/grails-mailgunn"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
    def developers = [ [ name: "Mariano Kfuri", email: "orquito@gmail.com" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "GitHub", url: "https://github.com/orkonano/grails-mailgun/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/orkonano/grails-mailgun.git" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    Closure doWithSpring() {{->
        restBuilder(RestBuilder, [:])

        emailHtmlRender(DefaultEmailHtmlRender){
            groovyPageRenderer = ref('groovyPageRenderer')
        }

    }}

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
