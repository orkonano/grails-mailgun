package grails.plugin.mailgun

import grails.plugin.mailgun.render.EmailHtmlRender
import grails.plugins.rest.client.RestBuilder
import grails.plugins.rest.client.RestResponse
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.json.JSONObject
import org.jsoup.Jsoup
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.IgnoreRest
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(MailgunService)
class MailgunServiceSpec extends Specification {

    def setup() {
        grailsApplication.config.mailgun.message.defaultFrom = "defaultForm"
        grailsApplication.config.mailgun.message.defaultTo = "defaultTo"
        grailsApplication.config.mailgun.message.defaultSubject = "defaultSubject"

        grailsApplication.config.mailgun.message.format = "HTML"

        grailsApplication.config.mailgun.message.defaulTemplate = "defaultTemplate"
        grailsApplication.config.mailgun.domain = "domain"
        grailsApplication.config.mailgun.apiKey = "apiKey"
        grailsApplication.config.mailgun.tracking.enabled = true
        grailsApplication.config.mailgun.tracking.clicks.enabled = true
        grailsApplication.config.mailgun.tracking.opens.enabled = true
        grailsApplication.config.mailgun.message.defaultReplyTo = "defaultReplyTo"

    }

    def cleanup() {
    }

    void "test getAllLists success"() {
        setup:
        def restBuilder = mockFor(RestBuilder.class)
        restBuilder.demand.get(1){String url, Closure cl ->
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

        this.service.restBuilder = restBuilder.createMock()

        when: "get all lists into mailgun account"
        RestResponse restResponse = this.service.allLists

        then: "The response is Status 200"
        restResponse.json.items
        restResponse.json.total_count == 2
        restResponse.status == HttpStatus.OK.value()
    }

    void "test sendMessage without parameters"() {
        setup:
        def restBuilder = mockFor(RestBuilder.class)

        def fromResult = ""
        def toResult = ""
        restBuilder.demand.post(1){String url, Closure cl ->
            new RestResponse(new ResponseEntity<String>(
                    """{
                      "message": "Queued. Thank you.",
                      "id": "<20111114174239.25659.5817@samples.mailgun.org>"
                    }""", HttpStatus.OK))
        }

        this.service.restBuilder = restBuilder.createMock()

        def emailHtmlRender = mockFor(EmailHtmlRender.class)
        emailHtmlRender.demand.render(1){Map params ->
            "<div>Test</div>"
        }

        when: "Send an email with all the params empty"
        RestResponse restResponse = this.service.sendMessage()

        then: "The response is OK: 200"
        restResponse.status == HttpStatus.OK.value()
    }

    @IgnoreRest
    void "test sendMessage with parameters"() {
        setup:
        def restBuilder = mockFor(RestBuilder.class)

        restBuilder.demand.post(1){String url, Closure cl ->

            new RestResponse(new ResponseEntity<String>(
                    """{
                      "message": "Queued. Thank you.",
                      "id": "<20111114174239.25659.5817@samples.mailgun.org>"
                    }""", HttpStatus.OK))
        }

        this.service.restBuilder = restBuilder.createMock()

        def emailHtmlRender = mockFor(EmailHtmlRender.class)
        emailHtmlRender.demand.render(1){Map params ->
            "<div>Test</div>"
        }

        when: "Send an email with all the params empty"
        RestResponse restResponse = this.service.sendMessage([FROM: "test@from.com"])

        then: "The response is OK: 200"
        restResponse.status == HttpStatus.OK.value()
    }
}
