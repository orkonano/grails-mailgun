# first the sources
cp -rf ../$1/src/groovy/* src/main/groovy
cp -rf ../$1/src/java/* src/main/groovy
cp -rf ../$1/grails-app/* grails-app
#cp -rf ../$1/$2 src/main/groovy/grails/$3
# then the tests
cp -rf ../$1/test/unit/* src/test/groovy
cp -rf ../$1/test/integration/* src/integration-test/groovy

# then templates / other resources
cp -rf ../$1/src/templates/* src/main/templates
