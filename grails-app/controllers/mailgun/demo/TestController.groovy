package mailgun.demo

import grails.plugins.rest.client.RestResponse

class TestController {

    def mailgunService

    def index() {
        RestResponse resp = mailgunService.allLists

        render resp.json.items
    }

    def send() {
        RestResponse resp = mailgunService.sendMessage()

        render resp.status
    }
}
