package grails.plugin.mailgun.model

import grails.validation.Validateable

/**
 * Created by orko on 17/10/15.
 */
class MailCampaign implements Validateable{

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
