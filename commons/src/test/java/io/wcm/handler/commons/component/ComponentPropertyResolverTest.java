/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2019 wcm.io
 * %%
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
 * #L%
 */
package io.wcm.handler.commons.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
@SuppressWarnings("deprecation")
class ComponentPropertyResolverTest {

  private final AemContext context = new AemContext();

  @Test
  void testResourceWithoutResourceType() {
    Resource resource = context.create().resource("/content/r1");

    ComponentPropertyResolver underTest = new ComponentPropertyResolver(resource);
    assertNull(underTest.get("prop1", String.class));
    assertEquals("def", underTest.get("prop1", "def"));
  }

  @Test
  void testResourceWithComponent() {
    Resource component = context.create().resource("/apps/app1/components/comp1",
        "prop1", "value1");
    Resource resource = context.create().resource("/content/r1",
        "sling:resourceType", component.getPath());

    ComponentPropertyResolver underTest = new ComponentPropertyResolver(resource);
    assertEquals("value1", underTest.get("prop1", String.class));
    assertEquals("value1", underTest.get("prop1", "def"));
  }

  @Test
  void testResourceWithSuperComponent() {
    Resource superComponent = context.create().resource("/apps/app1/components/comp2",
        "prop1", "value1");
    Resource component = context.create().resource("/apps/app1/components/comp1",
        "sling:resourceSuperType", superComponent.getPath());
    Resource resource = context.create().resource("/content/r1",
        "sling:resourceType", component.getPath());

    ComponentPropertyResolver underTest = new ComponentPropertyResolver(resource);
    assertEquals("value1", underTest.get("prop1", String.class));
    assertEquals("value1", underTest.get("prop1", "def"));
  }

}
