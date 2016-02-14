package grails.plugin.mailgun.render

import grails.gsp.PageRenderer
import org.springframework.beans.factory.InitializingBean

/**
 * Created by orko on 10/11/15.
 */
class DefaultEmailHtmlRender implements EmailHtmlRender, InitializingBean{

    PageRenderer groovyPageRenderer

    @Override
    void afterPropertiesSet() throws Exception {
        assert groovyPageRenderer != null, "PageRenderer is not set"
    }

    String render(Map params){
        groovyPageRenderer.render view: params.view, model: params.model
    }

}
