/*
 * Copyright 2009-2011 the original author or authors.
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

import griffon.core.GriffonApplication
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.python.util.PythonInterpreter

/**
 * @author Eric Wendelin
 */
class JythonGriffonAddon {
   private String jythonPropertyName

   void addonInit(GriffonApplication app) {
      jythonPropertyName = app.config.griffon?.jython?.dynamicPropertyName ?: 'py'
      if(jythonPropertyName) {
          jythonPropertyName = jythonPropertyName[0].toUpperCase() + jythonPropertyName[1..-1]
      } else {
          jythonPropertyName = 'Py'
      }
   }

   def events = [
      BootstrapStart: { app ->
          // Load Jython resources on bootstrap
          loadSources(app, 'classpath*:/jython/**/*.py')
      },
      NewInstance: { klass, type, instance ->
         // Inject jython property into all Controller class instances
         def types = app.config.griffon?.jython?.injectInto ?: ['controller']
         if(!types.contains(type)) return
          instance.metaClass."get${jythonPropertyName}" = { /* TODO: create JYTHON_PROXY? */ }
         instance.metaClass."${jythonPropertyName[0].toLowerCase() + jythonPropertyName[1..-1]}Load" = loadSources.curry(app)
      }
   ]

   private loadSources = { GriffonApplication app, String path ->
      def pathResolver = new PathMatchingResourcePatternResolver(app.class.classLoader)
      Class compilerClass = app.class.classLoader.loadClass('org.python.util.PythonInterpreter')
      def pythonInterpreter = compilerClass.newInstance()
      pathResolver.getResources(path).each {
         pythonInterpreter.exec it.getURL().getText()
      }
   }
}
