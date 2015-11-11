import grails.plugin.mailgun.render.DefaultEmailHtmlRender
import grails.plugins.rest.client.RestBuilder

/**
 * Created by orko on 10/11/15.
 */

beans = {

    restBuilder(RestBuilder, [:])

    emailHtmlRender(DefaultEmailHtmlRender){
        groovyPageRenderer = ref('groovyPageRenderer')
    }
}