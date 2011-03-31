/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Eric Wendelin
 * @since 0.1
 */

includeTargets << griffonScript("Init")
includePluginScript("jython", "_JythonCommon")

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('jython')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-jython-plugin', dirs: "${jythonPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('jython', [
        conf: 'compile',
        name: 'griffon-jython-addon',
        group: 'org.codehaus.griffon.plugins',
        version: jythonPluginVersion
    ])
}

eventCompileStart = {
    if(compilingPlugin('jython')) return
    compileJythonSrc()
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == "src.commons"} ) {
        pathToInfo << [name: "Common Sources", path: "src.commons", filetype: [".groovy",".java"]]
    }
    // TODO -- match multiline comments
    if(!pathToInfo.find{ it.path == "src.jython"} ) {
        def EMPTY = /^\s*$/
        pathToInfo << [name: "Jython Sources", path: "src.jython", filetype: [".py"], locmatcher: {file ->
            def loc = 0
            file.eachLine { line ->
                if(line ==~ EMPTY || line ==~ /^\s*\#.*/) return
                loc++
            }
            loc
        }]
    }
}
