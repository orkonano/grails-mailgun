package mailgun

import grails.plugins.rest.client.RestResponse

class TestController {

    def mailgunService

    def index() {
        RestResponse resp = mailgunService.allLists

        render resp.json.items
    }
}
