/*
 * Copyright 2010-2012 the original author or authors.
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
 * Common Gant targets for Jython plugin
 * 
 * @author Eric Wendelin
 * @since 0.1
 */

import griffon.util.GriffonExceptionHandler

includePluginScript('lang-bridge', '_Commons')

import org.python.core.PyException
import org.python.core.PySystemState
import org.python.core.imp as JythonImp
import org.python.modules._py_compile

target(name: 'compileJythonSrc', description: "", prehook: null, posthook: null) {
    depends(parseArguments, compileCommons)

    def jythonsrc = "${basedir}/src/jython"
    def jythonsrcdir = new File(jythonsrc)
    if(!jythonsrcdir.exists() || !jythonsrcdir.list().size()) {
        ant.echo(message: "[jython] No Jython sources were found.")
        return
    }

    if(sourcesUpToDate("${basedir}/src/jython", projectMainClassesDir, ".py")) return

    ant.echo(message: "[jython] Compiling Jython sources to $projectMainClassesDir")
    try {
        defineJythonCompilePath(jythonsrc, projectMainClassesDir)
        compileJythonFiles(jythonsrcdir, projectMainClassesDir)
    }
    catch (Exception e) {
        if(argsMap.compileTrace) {
            GriffonExceptionHandler.sanitize(e)
            e.printStackTrace(System.err)
        }
        event("StatusFinal", ["Compilation error: ${e.message}"])
        exit(1)
    }
}

target(name: 'compileJythonTest', description: "", prehook: null, posthook: null) {
    depends(parseArguments)

    def jythontest = "${basedir}/test/jython"
    def jythontestdir = new File(jythontest)
    if(!jythontestdir.exists() || !jythontestdir.list().size()) {
        ant.echo(message: "[jython] No Jython tests sources were found.")
        return
    }

    def destdir = new File(griffonSettings.testClassesDir, "jython")
    ant.mkdir(dir: destdir)

    if(sourcesUpToDate(jythontest, destdir.absolutePath, ".py")) return

    ant.echo(message: "[jython] Compiling Jython test sources to $destdir")
    try {
        defineJythonTestPath(jythontest, destdir)
		compileJythonFiles(jythontestdir, destdir)
    }
    catch (Exception e) {
        if(argsMap.compileTrace) {
            GriffonExceptionHandler.sanitize(e)
            e.printStackTrace(System.err)
        }
        event("StatusFinal", ["Compilation error: ${e.message}"])
        exit(1)
    }
}

defineJythonCompilePath = { srcdir, destdir ->
    ant.path(id: "jython.compile.classpath") {
        path(refid: "griffon.compile.classpath")
        pathElement(location: destdir)
        pathElement(location: srcdir)
    }

    if (argsMap.compileTrace) {
        println('-' * 80)
        println "[GRIFFON] 'jython.compile.classpath' entries"
        ant.project.getReference('jython.compile.classpath').list().each {println("  $it")}
        println('-' * 80)
    }
}

defineJythonTestPath = { srcdir, destdir ->
    ant.path(id: "jython.test.classpath") {
        path(refid: "jython.compile.classpath")
        pathElement(location: destdir)
        pathElement(location: srcdir)
    }

    if (argsMap.compileTrace) {
        println('-' * 80)
        println "[GRIFFON] 'jython.test.classpath' entries"
        ant.project.getReference('jython.test.classpath').list().each {println("  $it")}
        println('-' * 80)
    }
}

compileJythonFiles = { jythonsrcdir, projectMainClassesDir ->
	ant.taskdef(name: 'jycompile', classname: 'org.python.util.JycompileAntTask', classpathref: 'jython.compile.classpath')
	ant.jycompile(srcdir: jythonsrcdir, destdir: projectMainClassesDir) {
		classpath { path(refid: 'jython.compile.classpath') }
	}
}
