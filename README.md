[![Build Status](https://travis-ci.org/orkonano/grails-mailgun.svg?branch=develop)](https://travis-ci.org/orkonano/grails-mailgun.svg?branch=master)
[![Project Status](https://stillmaintained.com/orkonano/grails-mailgun.png)](https://stillmaintained.com/orkonano/grails-mailgun)
[![Stories in Ready](https://badge.waffle.io/orkonano/grails-mailgun.svg?label=ready&title=Ready)](http://waffle.io/orkonano/grails-mailgun)

# grails-mailgun
Grails plugin to use Mailgun Api.

**¡IMPORTANT!**
You need to create your own mailgun account. The plugin doesn't work without a valid api-key and a valid domain

## Config parameters

You need to set into your Config.groovy the following properties:

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

The plugin allows to work with some features of mailgun:
- Lists all mail list created in mailgun: Mailgun endpoint --> GET: https://api.mailgun.net/v3/lists
- Sends message across Mailgun Api: Mailgun endpoint --> POST: https://api.mailgun.net/v3/$domain/messages


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

The method *allLists*, lists all mail lists created in mailgun.

The method *sendMessage*, sends a new message across mailgun api.
