package grails.plugin.mailgun

import grails.plugin.mailgun.render.EmailHtmlRender
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import org.codehaus.groovy.grails.web.json.JSONObject
import org.jsoup.Jsoup

class MailgunService {

    RestBuilder restBuilder
    def grailsApplication
    EmailHtmlRender emailHtmlRender

    RestResponse getAllLists(){
        log.info("Buscando todas las listas de mail de mailgun")
        RestResponse resp = restBuilder.get("https://api.mailgun.net/v3/lists"){
            auth 'api', grailsApplication.config.mailgun.apiKey
            accept JSONObject
        }
        log.info("Se termino de buscar las listas de mailgun, con status $resp.statusCode")
        resp
    }

    RestResponse sendMessage(Map params = [:]){
        log.info("Enviando mensaje mediante mailgun")

        String from = getMailgunProperty(MailgunParameters.FROM.name(), params, grailsApplication.config.mailgun.message.defaultFrom)
        String[] recipients = getMailgunProperty(MailgunParameters.TO.name(), params, grailsApplication.config.mailgun.message.defaultTo).split(',')
        String subject = getMailgunProperty(MailgunParameters.SUBJECT.name(), params, grailsApplication.config.mailgun.message.defaultSubject)

        String format = getMailgunProperty(MailgunParameters.FORMAT.name(), params, grailsApplication.config.mailgun.message.format)

        String bodyHtml = ""
        String bodyText = ""
        if (format == 'html'){
            def bodyParams = getMailgunProperty(MailgunParameters.BODY.name(), params, [:])
            String template = getMailgunProperty(MailgunParameters.TEMPLATE.name(), params, grailsApplication.config.mailgun.message.defaulTemplate)
            bodyHtml = emailHtmlRender.render([view: template, model: bodyParams])
            bodyText = Jsoup.parse(bodyHtml).text()

        }else{
            bodyText = getMailgunProperty(MailgunParameters.BODY.name(), params, "")
        }

        String[] tags = getMailgunProperty(MailgunParameters.TAG.name(), params, "").split(",")
        String[] campaignsID = getMailgunProperty(MailgunParameters.CAMPAIGN_ID.name(), params, "").split(",")

        RestResponse resp = restBuilder.post("https://api.mailgun.net/v3/$grailsApplication.config.mailgun.domain/messages"){
            auth 'api', grailsApplication.config.mailgun.apiKey
            accept JSONObject
            setProperty 'from', from
            recipients.each { to ->
                setProperty 'to', to.trim()
            }
            setProperty 'subject', subject

            if (bodyHtml){
                setProperty 'html', bodyHtml
            }
            setProperty 'text', bodyText

            setProperty 'o:tracking', grailsApplication.config.mailgun.tracking.enabled
            setProperty 'o:tracking-clicks', grailsApplication.config.mailgun.tracking.clicks.enabled
            setProperty 'o:tracking-opens', grailsApplication.config.mailgun.tracking.opens.enabled
            setProperty 'h:Reply-To', grailsApplication.config.mailgun.message.defaultReplyTo

            if (campaignsID){
                campaignsID.each { campaignID ->
                    setProperty 'o:campaign', campaignID.trim()
                }
            }

            if (tags){
                tags.each { tag ->
                    setProperty 'o:tag', tag.trim()
                }
            }
        }

        log.info("Se termino de enviar el mail, con status $resp.statusCode")
        resp
    }

    private getMailgunProperty(String property, Map params, defaultValue = ""){
        params[property] ?: defaultValue
    }
}
