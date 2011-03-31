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
 * Gant script that creates a Jython class
 * 
 * @author Eric Wendelin
 * @since 0.1
 */

import org.codehaus.griffon.commons.GriffonClassUtils as GCU

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

target(default: "Creates a new Jython class") {
    depends(checkVersion, parseArguments)
    promptForName(type: "Class")
    def (pkg, name) = extractArtifactName(argsMap["params"][0])
    pkg = pkg ?: "griffon"
    def clazzName = GCU.getClassNameRepresentation(name)
    def clazz = "${pkg}.${clazzName}"

    def packageDir = new File("${basedir}/src/jython/${pkg.replace('.','/')}")
    println packageDir
    packageDir.mkdirs()

    def classFile = new File(packageDir, "${name}.py") 
    if(classFile.exists()) {
        if(!confirmInput("WARNING: ${clazz} already exists.  Are you sure you want to replace it?")) {
            exit(0)
        }
    }
    println "Creating ${clazz} ..."
    classFile.text = """\
class ${clazzName}:
    def __init__(self):
        print 'Hello ${clazzName}'
"""
}
