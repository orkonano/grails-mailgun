
withConfig(configuration) {
    inline(phase: 'CONVERSION') { source, context, classNode ->
        classNode.putNodeMetaData('projectVersion', '2.0.0-SNAPSHOT')
        classNode.putNodeMetaData('projectName', 'grails-mailgun')
        classNode.putNodeMetaData('isPlugin', 'true')
    }
}
