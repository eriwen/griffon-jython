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
 * Common Gant targets for Jython plugin
 * 
 * @author Eric Wendelin
 * @since 0.1
 */

import org.python.core.PyException
import org.python.core.PySystemState
import org.python.core.imp as JythonImp
import org.python.modules._py_compile

target(compileJythonSrc: "") {
	includePluginScript("lang-bridge", "CompileCommons")
    compileCommons()
    def jythonsrc = "${basedir}/src/jython"
    def jythonsrcdir = new File(jythonsrc)
    if(!jythonsrcdir.exists() || !jythonsrcdir.list().size()) {
        ant.echo(message: "[jython] No Jython sources were found.")
        return
    }

    if(sourcesUpToDate("${basedir}/src/jython", classesDirPath, ".py")) return

    ant.echo(message: "[jython] Compiling Jython sources to $classesDirPath")
    try {
        defineJythonCompilePath(jythonsrc, classesDirPath)
        compileJythonFiles(jythonsrcdir, classesDirPath)
    }
    catch (Exception e) {
        ant.fail(message: "Could not compile Jython sources: " + e.class.simpleName + ": " + e.message)
    }
}

target(compileJythonTest: "") {
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
        ant.fail(message: "Could not compile Jython test sources: " + e.class.simpleName + ": " + e.message)
    }
}

defineJythonCompilePath = { srcdir, destdir ->
    ant.path(id: "jython.compile.classpath") {
        path(refid: "griffon.compile.classpath")
        fileset(dir: "${getPluginDirForName('jython').file}/lib", includes: "*.jar")
        path(location: destdir)
        path(location: srcdir)
    }
}

defineJythonTestPath = { srcdir, destdir ->
    ant.path(id: "jython.test.classpath") {
        path(refid: "jython.compile.classpath")
        path(location: destdir)
        path(location: srcdir)
    }
}

compileJythonFiles = { jythonsrcdir, classesDirPath ->
	ant.taskdef(name: 'jycompile', classname: 'org.python.util.JycompileAntTask', classpathref: 'jython.compile.classpath')
	ant.jycompile(srcdir: jythonsrcdir, destdir: classesDirPath) {
		classpath { path(refid: 'jython.compile.classpath') }
	}
}

private boolean compilingJythonPlugin() { getPluginDirForName("jython") == null }
