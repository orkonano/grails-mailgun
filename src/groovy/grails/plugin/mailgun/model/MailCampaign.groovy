package grails.plugin.mailgun.model

import grails.validation.Validateable

/**
 * Created by orko on 17/10/15.
 */
@Validateable
class MailCampaign {

    String recipients
    String subject
    String body
    String tags
    String campaignId

    static constraints = {
        tags nullable: true, blank: true
        campaignId nullable: true, blank: true
    }

}
