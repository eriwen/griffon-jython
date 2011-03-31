griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
    }
    dependencies {
        build   'org.python:jython:2.5.0'
        compile 'org.python:jython:2.5.0'

        def springVersion = '3.0.5.RELEASE'
        compile("org.springframework:org.springframework.core:$springVersion") {
            transitive = false
        }
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}

griffon.jars.destDir='target/addon'
