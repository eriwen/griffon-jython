/*
 * Copyright 2011 the original author or authors.
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
 */
class JythonGriffonPlugin {
    def version = '0.2'
    def dependsOn = ['lang-bridge': '0.4.2']
    def griffonVersion = '0.9.2 > *'
    def license = 'Apache Software License 2.0'

    def author = 'Eric Wendelin'
    def authorEmail = 'emwendelin@gmail.com'
    def title = 'Enables Jython'
    def description = '''
Enables the usage of Jython on your Griffon application.
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Jython+Plugin'
}
