/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.plugins.groovy.lang.resolve.dgm

import com.intellij.testFramework.LightProjectDescriptor
import groovy.transform.CompileStatic
import org.jetbrains.plugins.groovy.GroovyLightProjectDescriptor
import org.jetbrains.plugins.groovy.lang.psi.api.statements.typedef.members.GrGdkMethod
import org.jetbrains.plugins.groovy.lang.resolve.GroovyResolveTestCase

@CompileStatic
class ResolveDGMMethod23Test extends GroovyResolveTestCase {

  LightProjectDescriptor projectDescriptor = GroovyLightProjectDescriptor.GROOVY_2_3

  void testIsNumber() {
    def resolved = resolveByText('"1.2.3".isN<caret>umber()', GrGdkMethod)
    assert resolved.staticMethod.containingClass.qualifiedName == 'org.codehaus.groovy.runtime.StringGroovyMethods'
  }

  void testSort() {
    def resolved = resolveByText('[].so<caret>rt()', GrGdkMethod)
    def parameterList = resolved.staticMethod.parameterList.parameters
    assert parameterList.size() == 1
    assert parameterList[0].type.canonicalText == 'java.lang.Iterable<T>'
  }

  // TODO groovy-all-2.3.0.jar was stripped to run on java 6. Unignore when it will contain NioGroovyMethods
  @SuppressWarnings("GroovyUnusedDeclaration")
  void _testCloseable() {
    myFixture.addClass('package java.io; class Closeable {}');
    def resolved = resolveByText('''\
class A implements Closeable {}
new A().withC<caret>loseable {}
''', GrGdkMethod)
    assert resolved.staticMethod.containingClass.qualifiedName == 'org.codehaus.groovy.runtime.NioGroovyMethods'
  }
}
