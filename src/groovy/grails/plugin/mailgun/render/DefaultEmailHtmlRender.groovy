package grails.plugin.mailgun.render

import grails.gsp.PageRenderer

/**
 * Created by orko on 10/11/15.
 */
class DefaultEmailHtmlRender implements EmailHtmlRender{

    PageRenderer groovyPageRenderer

    String render(Map params){
        groovyPageRenderer.render view: params.view, model: params.model
    }
}
