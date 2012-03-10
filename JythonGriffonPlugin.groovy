/*
 * Copyright 2011-2012 the original author or authors.
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
    // the plugin version
    String version = '0.3'
    // the version or versions of Griffon the plugin is designed for
    String griffonVersion = '0.9.5 > *'
    // the other plugins this plugin depends on
    Map dependsOn = ['lang-bridge': '0.5']
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []
    // URL where documentation can be found
    String documentation = 'http://griffon.codehaus.org/Jython+Plugin'
    // URL where source can be found
    String source = 'https://github.com/eriwen/griffon-jython'

    List authors = [
        [
            name: 'Eric Wendelin',
            email: 'emwendelin@gmail.com'
        ]
    ]
    String title = 'Brings the Jython language compiler and libraries'
    // accepts Markdown syntax. See http://daringfireball.net/projects/markdown/ for details
    String description = '''
The Jython plugin enables compiling and running [Jython][1] code on your Griffon application.

Usage
-----

You must place all Jython code under `$appdir/src/jython`, it will be compiled first before any sources
available on `griffon-app` or `src/main` which means you can't reference any of those sources from your
Jython code, while the Groovy sources can. You will be able to use the [LangBridge Plugin][2] facilities to
communicate with other JVM languages.

Sources placed under `$appdir/src/jython` will generate Java classes available for use within griffon-app.

__$appdir/src/jython/griffon/JythonGreeter.py__

    from org.codehaus.griffon import IGreeter  
    class JythonGreeter(IGreeter):
        def __init__(self):
            pass
    
        def greet(self, who, model):
            greeting = 'Hello %s from Jython!' % str(who)
            print greeting
            model.setOutput(greeting)

This will generate a Java class named `griffon.JythonGreeter`, which can be used inside any Griffon artifact,
for example a Controller

    import javax.swing.JOptionPane
    import java.beans.PropertyChangeListener
    import org.codehaus.griffon.IGreeter
    import griffon.jython.JythonObjectFactory
    
    class JyAppController {
        def model
        def view
        def greeter
    
        def mvcGroupInit(Map args) {
             model.addPropertyChangeListener("output", { evt ->
                 if(!evt.newValue) return
                 // model.output may have been updated outside EDT
                 doLater {
                     JOptionPane.showMessageDialog(app.windowManager.windows[0],
                         evt.newValue, "Yay for Jython", JOptionPane.INFORMATION_MESSAGE)
                 }
             } as PropertyChangeListener)
    
             // Create our JythonGreeter
             JythonObjectFactory factory = new JythonObjectFactory(IGreeter.class, 'JythonGreeter', 'JythonGreeter')
             greeter = (IGreeter) factory.createObject()
         }
    
        def handleClick = { evt = null ->
            if(!model.input) return
            // clear the result first
            model.output = ""
            // invoke Jython class outside the EDT
            doOutside {
                greeter.greet(model.input, model)
            }
        }
    }

You are also able to load Jython scripts at any time. By default all scripts placed at `$basedir/griffon-app/resources/jython`
will be loaded when the application boostraps itself. For example `griffon-app/resources/jython/fib.py` might look like this:

    def addNumbers(a, b):
         return a + b

With that code in place, the addNumbers function may be executed as a method call on a dynamic property named `py` from a
Griffon controller. See below:

    class FooController {
        def addNumbers = { evt = null ->
           // invoke the function as a method on the 'py' dynamic property
           model.z = py.add_numbers(model.x, model.y)
        }
    }

The dynamic property will be named `py` by default. The name of the property may be set explicitly in
`griffon-app/conf/Config.groovy` by assigning a value to the griffon.jython.dynamicPropertyName property.

    griffon.jython.dynamicPropertyName = 'jythonPropertyName'

For most applications, the default name of `py` should be fine. You can also alter in which artifacts the property gets
injected, by default only controllers will have that property. See `griffon-app/conf/Config.groovy` and look for the following entry

    griffon.jython.injectInto = ["controller"]

Finally, you can load any Jython script by calling `pyLoad(String path)` where `path` will be resolved using Spring's
PathMatchingResourcePatternResolver. The default path used during bootstrap is `"classpath*:/jython/**/*.py"`.
It is also worth mentioning that this method will be injected to all artifacts controlled by `griffon.jython.injectInto`
and that the prefix `py` will be affected by `griffon.jython.dynamicPropertyName`.

Scripts
-------

 * **create-jython-class** - creates a new Jython class in `$basedir/src/jython`.
 * **create-jython-script** - creates a new Jython script in `$basedir/griffon-app/resources/py`.
 * **jython-repl** - executes a Jython REPL with the application's classpath fully configured.

[1]: http://www.jython.org
[2]: /plugin/lang-bridge
'''
}