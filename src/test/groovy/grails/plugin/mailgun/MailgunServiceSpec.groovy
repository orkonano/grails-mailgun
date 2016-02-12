package grails.plugin.mailgun

import grails.converters.JSON
import grails.plugin.mailgun.render.EmailHtmlRender
import grails.plugins.rest.client.RequestCustomizer
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.TestFor
import grails.web.http.HttpHeaders
import org.apache.commons.codec.binary.Base64
import org.grails.web.json.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(MailgunService)

class MailgunServiceSpec extends Specification {

    String keyAuthorization

    def setup() {
        grailsApplication.config.mailgun.message.defaultFrom = "defaultForm"
        grailsApplication.config.mailgun.message.defaultTo = "defaultTo"
        grailsApplication.config.mailgun.message.defaultSubject = "defaultSubject"

        grailsApplication.config.mailgun.message.format = "html"

        grailsApplication.config.mailgun.message.defaulTemplate = "defaultTemplate"
        grailsApplication.config.mailgun.domain = "domain"
        grailsApplication.config.mailgun.apiKey = "apiKey"
        grailsApplication.config.mailgun.tracking.enabled = true
        grailsApplication.config.mailgun.tracking.clicks.enabled = true
        grailsApplication.config.mailgun.tracking.opens.enabled = true
        grailsApplication.config.mailgun.message.defaultReplyTo = "defaultReplyTo"
        keyAuthorization = new String(Base64.encodeBase64("api:$grailsApplication.config.mailgun.apiKey".bytes))

    }

    def cleanup() {
    }

    void "test getAllLists success"() {
        setup:
        def restBuilder = Mock(RestBuilder.class)
        restBuilder.get(_, _) >> { String url, Closure cl ->
            new RestResponse(new ResponseEntity<String>(
                    """{
                      \"items\": [
                           {
                               \"access_level\": \"readonly\",
                               \"address\": \"list@test.com\",
                               \"created_at\": \"Tue, 17 Nov 2015 22:20:13 -0000\",
                               \"description\": \"Mailgun developers list\",
                               \"members_count\": 0,
                               \"name\": \"\"
                           },
                           {
                               \"access_level\": \"readonly\",
                               \"address\": \"test-valid-email@test.comr\",
                               \"created_at\": \"Thu, 12 Nov 2015 19:57:21 -0000\",
                               \"description\": \"Test Valid Email\",
                               \"members_count\": 1,
                               \"name\": \"Test Name\"
                           }
                      ],
                    \"total_count\": 2
                 }""", HttpStatus.OK))
        }

        this.service.restBuilder = restBuilder


        when: "get all lists into mailgun account"
        RestResponse restResponse = this.service.allLists

        then: "The response is Status 200"
        restResponse.json.items
        restResponse.json.total_count == 2
        restResponse.status == HttpStatus.OK.value()
    }

    void "test sendMessage without parameters and default HTML Format"() {
        setup:
        def restBuilder = Mock(RestBuilder.class)

        def requestCustomizer = new RequestCustomizer()
        restBuilder.post(_, _) >> { String url, Closure cl ->
            cl.delegate = requestCustomizer
            cl.call()
            new RestResponse(new ResponseEntity<String>(
                    """{
                      "message": "Queued. Thank you.",
                      "id": "<20111114174239.25659.5817@samples.mailgun.org>"
                    }""", HttpStatus.OK))
        }

        this.service.restBuilder = restBuilder

        def emailHtmlRender = Mock(EmailHtmlRender.class)
        emailHtmlRender.render(_) >> { Map params ->
            "<div>" + params.model + "</div>"
        }

        this.service.emailHtmlRender = emailHtmlRender

        when: "Send an email with all the params empty"
        RestResponse restResponse = this.service.sendMessage([BODY: "bodyTest"])

        then: "The response is OK: 200"
        restResponse.status == HttpStatus.OK.value()
        requestCustomizer.mvm['from'] == [grailsApplication.config.mailgun.message.defaultFrom]
        requestCustomizer.mvm['to'] == [grailsApplication.config.mailgun.message.defaultTo]
        requestCustomizer.mvm['subject'] == [grailsApplication.config.mailgun.message.defaultSubject]
        requestCustomizer.mvm['html'] == ["<div>bodyTest</div>"]
        requestCustomizer.mvm['text'] == ["bodyTest"]
        requestCustomizer.mvm['o:tracking'] == [grailsApplication.config.mailgun.tracking.enabled]
        requestCustomizer.mvm['o:tracking-clicks'] == [grailsApplication.config.mailgun.tracking.clicks.enabled]
        requestCustomizer.mvm['o:tracking-opens'] == [grailsApplication.config.mailgun.tracking.opens.enabled]
        requestCustomizer.mvm['h:Reply-To'] == [grailsApplication.config.mailgun.message.defaultReplyTo]
        !requestCustomizer.mvm['o:campaign']
        !requestCustomizer.mvm['o:tag']
        requestCustomizer.headers.get(HttpHeaders.AUTHORIZATION) == ["Basic $keyAuthorization"]
        requestCustomizer.acceptType == JSONObject
    }

    void "test sendMessage with parameters and HTML format"() {
        setup:
        def restBuilder = Mock(RestBuilder.class)

        def requestCustomizer = new RequestCustomizer()
        restBuilder.post(_, _) >> { String url, Closure cl ->
            cl.delegate = requestCustomizer
            cl.call()
            new RestResponse(new ResponseEntity<String>(
                    """{
                      "message": "Queued. Thank you.",
                      "id": "<20111114174239.25659.5817@samples.mailgun.org>"
                    }""", HttpStatus.OK))
        }

        this.service.restBuilder = restBuilder

        def emailHtmlRender = Mock(EmailHtmlRender.class)
        emailHtmlRender.render(_) >> { Map params ->
            "<div>" + params.model + "</div>"
        }

        this.service.emailHtmlRender = emailHtmlRender

        when: "Send an email with all the params empty"
        RestResponse restResponse = this.service.sendMessage([FROM: "test@from.com", TO: "test@to.com", SUBJECT: "SubjectTest",
                                                              FORMAT: "html", BODY: "bodyTest", TEMPLATE: "templateTest",
                                                              TAG: "tagTest1,tagTest2", CAMPAIGN_ID: "campaignTest"])
        then: "The response is OK: 200"
        restResponse.status == HttpStatus.OK.value()
        requestCustomizer.mvm['from'] == ["test@from.com"]
        requestCustomizer.mvm['to'] == ["test@to.com"]
        requestCustomizer.mvm['subject'] == ["SubjectTest"]
        requestCustomizer.mvm['html'] == ["<div>bodyTest</div>"]
        requestCustomizer.mvm['text'] == ["bodyTest"]
        requestCustomizer.mvm['o:tracking'] == [grailsApplication.config.mailgun.tracking.enabled]
        requestCustomizer.mvm['o:tracking-clicks'] == [grailsApplication.config.mailgun.tracking.clicks.enabled]
        requestCustomizer.mvm['o:tracking-opens'] == [grailsApplication.config.mailgun.tracking.opens.enabled]
        requestCustomizer.mvm['h:Reply-To'] == [grailsApplication.config.mailgun.message.defaultReplyTo]
        requestCustomizer.mvm['o:campaign'] == ["campaignTest"]
        requestCustomizer.mvm['o:tag'] == ["tagTest1","tagTest2"]
        requestCustomizer.headers.get(HttpHeaders.AUTHORIZATION) == ["Basic $keyAuthorization"]
        requestCustomizer.acceptType == JSONObject
    }

    void "test sendMessage with parameters and TEXT formar"() {
        setup:
        def restBuilder = Mock(RestBuilder.class)

        def requestCustomizer = new RequestCustomizer()
        restBuilder.post(_, _) >> { String url, Closure cl ->
            cl.delegate = requestCustomizer
            cl.call()
            new RestResponse(new ResponseEntity<String>(
                    """{
                      "message": "Queued. Thank you.",
                      "id": "<20111114174239.25659.5817@samples.mailgun.org>"
                    }""", HttpStatus.OK))
        }

        this.service.restBuilder = restBuilder

        def emailHtmlRender = Mock(EmailHtmlRender.class)
        emailHtmlRender.render(_) >> { Map params ->
            "<div>" + params.model + "</div>"
        }

        this.service.emailHtmlRender = emailHtmlRender

        when: "Send an email with all the params empty"
        RestResponse restResponse = this.service.sendMessage([FROM: "test@from.com", TO: "test@to.com", SUBJECT: "SubjectTest",
                                                              FORMAT: "text", BODY: "bodyTest", TEMPLATE: "templateTest",
                                                              TAG: "tagTest1,tagTest2", CAMPAIGN_ID: "campaignTest"])
        then: "The response is OK: 200"
        restResponse.status == HttpStatus.OK.value()
        requestCustomizer.mvm['from'] == ["test@from.com"]
        requestCustomizer.mvm['to'] == ["test@to.com"]
        requestCustomizer.mvm['subject'] == ["SubjectTest"]
        !requestCustomizer.mvm['html']
        requestCustomizer.mvm['text'] == ["bodyTest"]
        requestCustomizer.mvm['o:tracking'] == [grailsApplication.config.mailgun.tracking.enabled]
        requestCustomizer.mvm['o:tracking-clicks'] == [grailsApplication.config.mailgun.tracking.clicks.enabled]
        requestCustomizer.mvm['o:tracking-opens'] == [grailsApplication.config.mailgun.tracking.opens.enabled]
        requestCustomizer.mvm['h:Reply-To'] == [grailsApplication.config.mailgun.message.defaultReplyTo]
        requestCustomizer.mvm['o:campaign'] == ["campaignTest"]
        requestCustomizer.mvm['o:tag'] == ["tagTest1","tagTest2"]
        requestCustomizer.headers.get(HttpHeaders.AUTHORIZATION) == ["Basic $keyAuthorization"]
        requestCustomizer.acceptType == JSONObject
    }

}
