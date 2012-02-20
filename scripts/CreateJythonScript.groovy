/*
 * Copyright 2010-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Gant script that creates a Jython script
 * 
 * @author Eric Wendelin
 * @since 0.1
 */

includeTargets << griffonScript("_GriffonCreateArtifacts")

target('default': "Creates a new Jython script") {
	depends(parseArguments)

	promptForName(type: 'Script')

	def name = argsMap["params"][0]
	def scriptDir = new File("./griffon-app/resources/jython")
	scriptDir.mkdirs()

	def scriptName = "${scriptDir.canonicalPath}/${name}.py"
	def scriptFile = new File(scriptName) 
	if(scriptFile.exists()) {
		if(!confirmInput("WARNING: ${scriptName} already exists.  Are you sure you want to replace this script?")) {
			exit(0)
		}
	}
	println "Creating ${scriptName} ..."
	scriptFile.text = """\
def main():
	print 'Hello ${name}!'

if __name__ == '__main__':
	main()
"""
	exit(0)
}
