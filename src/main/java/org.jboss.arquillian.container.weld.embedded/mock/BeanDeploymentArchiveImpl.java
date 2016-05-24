/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.jboss.weld.bootstrap.api.Environment;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.bootstrap.api.helpers.SimpleServiceRegistry;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.ejb.spi.EjbDescriptor;
import org.jboss.weld.injection.spi.EjbInjectionServices;
import org.jboss.weld.injection.spi.JpaInjectionServices;
import org.jboss.weld.injection.spi.ResourceInjectionServices;
import static java.util.Arrays.asList;
import static org.jboss.arquillian.container.weld.embedded.mock.Ejbs.createEjbDescriptors;
import static org.jboss.weld.bootstrap.spi.BeansXml.EMPTY_BEANS_XML;

/**
 * @author pmuir
 */
public class BeanDeploymentArchiveImpl implements BeanDeploymentArchive {

    private final Collection<String> beanClasses;
    private final BeansXml beansXml;
    private final Collection<EjbDescriptor<?>> ejbs;
    private final ServiceRegistry services;
    private final Collection<BeanDeploymentArchive> bdas;
    private final String id;

    public BeanDeploymentArchiveImpl(BeansXml beansXml, Iterable<Class<?>> classes, Environment environment) {
        this("test", beansXml, classes, environment);
    }

    public BeanDeploymentArchiveImpl(Iterable<Class<?>> classes) {
        this("test", EMPTY_BEANS_XML, classes, Environments.SE);
    }

    public BeanDeploymentArchiveImpl(Iterable<Class<?>> classes, Environment environment) {
        this("test", EMPTY_BEANS_XML, classes, environment);
    }

    public BeanDeploymentArchiveImpl(String id, Environment environment, Class<?>... classes) {
        this(id, EMPTY_BEANS_XML, asList(classes), environment);
    }

    public BeanDeploymentArchiveImpl(String id, Class<?>... classes) {
        this(id, EMPTY_BEANS_XML, asList(classes), Environments.SE);
    }

    public BeanDeploymentArchiveImpl(String id, BeansXml beansXml, Environment environment, Class<?>... classes) {
        this(id, beansXml, asList(classes), environment);
    }

    public BeanDeploymentArchiveImpl(String id, BeansXml beansXml, Class<?>... classes) {
        this(id, beansXml, asList(classes),  Environments.SE);
    }

    public BeanDeploymentArchiveImpl(String id, BeansXml beansXml, Iterable<Class<?>> beanClasses, Environment environment) {
        this.services = new SimpleServiceRegistry();
        this.bdas = new HashSet<BeanDeploymentArchive>();
        this.beanClasses = new ArrayList<String>();
        for (Class<?> clazz : beanClasses) {
            this.beanClasses.add(clazz.getName());
        }
        this.beansXml = beansXml;
        this.ejbs = createEjbDescriptors(beanClasses);
        this.id = id;
        configureServices(environment);
    }

    protected void configureServices(Environment environment) {
        if (environment.equals(Environments.EE) || environment.equals(Environments.EE_INJECT)) {
            this.services.add(EjbInjectionServices.class, new MockEjbInjectionServices());
            this.services.add(JpaInjectionServices.class, new MockJpaInjectionServices());
        }
        this.services.add(ResourceInjectionServices.class, new MockResourceInjectionServices());
    }

    public Collection<String> getBeanClasses() {
        return beanClasses;
    }

    public BeansXml getBeansXml() {
        return beansXml;
    }

    public Collection<BeanDeploymentArchive> getBeanDeploymentArchives() {
        return bdas;
    }

    public Collection<EjbDescriptor<?>> getEjbs() {
        return ejbs;
    }

    public ServiceRegistry getServices() {
        return services;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BeanDeploymentArchiveImpl) {
            BeanDeploymentArchiveImpl that = (BeanDeploymentArchiveImpl) obj;
            return this.getId().equals(that.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

}
