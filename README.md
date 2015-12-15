[![Build Status](https://travis-ci.org/orkonano/grails-mailgun.svg?branch=develop)](https://travis-ci.org/orkonano/grails-mailgun.svg?branch=master)
[![Project Status](https://stillmaintained.com/orkonano/grails-mailgun.png)](https://stillmaintained.com/orkonano/grails-mailgun)

# grails-mailgun
Grails plugin to use Mailgun Api.

**¡IMPORTANT!**
You need to create your own mailgun account. The plugins doens't work without a valid api-key and a valid domain

## Config parameters

You need to set into your Config.groovy the followings properties

```groovy
mailgun{
       apiKey = 'test'
       domain = 'test'

       message{
              defaultFrom = 'test'
              defaultTo = 'test'
              defaultSubject = 'BigHamlet tiene promociones para vos'
              format = 'html'
              defaulTemplate = '/test/mailgunTest'
              defaultReplyTo = 'test'
       }
}
```

The full list of properties is:

```groovy
mailgun{
       apiKey = 'test'
       domain = 'test'

       message{
              defaultFrom = 'test'
              defaultTo = 'test'
              defaultSubject = 'BigHamlet tiene promociones para vos'
              format = 'html'
              defaulTemplate = '/test/mailgunTest'
              defaultReplyTo = 'test'
       }

       tracking{
              enabled = 'yes'
              clicks{
                     enabled = 'yes'
              }
              opens{
                     enabled = 'yes'
              }
       }
}
```

## Mailgun Features

The plugin allows to work with some mailgun's features:
- List all maillist created in mailgun: Mailgun endpoint --> GET: https://api.mailgun.net/v3/lists
- Send message across Mailgun Api: Mailgun endpoint --> POST: https://api.mailgun.net/v3/$domain/messages


## Examples

```groovy
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
```

## Examples

```groovy
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
```

**MailgunService** implements all features.

The method *allLists*, lists all maillist created in mailgun.

The method *sendMessage*, send a new message across mailgun api.
