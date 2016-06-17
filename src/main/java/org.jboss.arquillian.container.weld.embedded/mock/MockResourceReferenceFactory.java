/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.container.weld.embedded.mock;

import org.jboss.weld.injection.spi.ResourceReference;
import org.jboss.weld.injection.spi.ResourceReferenceFactory;

public class MockResourceReferenceFactory implements ResourceReferenceFactory<Object>
{

   @SuppressWarnings("unchecked")
   public static <T> ResourceReferenceFactory<T> instance() {
      return (ResourceReferenceFactory<T>) INSTANCE;
   }

   private static final MockResourceReferenceFactory INSTANCE = new MockResourceReferenceFactory();

   private static final ResourceReference<Object> EMPTY_RESOURCE_REFERENCE = new ResourceReference<Object>() {

        public void release() {
        }

        public Object getInstance() {
            return null;
        }
   };

   private MockResourceReferenceFactory()
   {
   }

   @Override
   public ResourceReference<Object> createResource()
   {
      return EMPTY_RESOURCE_REFERENCE;
   }

}
