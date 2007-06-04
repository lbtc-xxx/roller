/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */

package org.apache.roller.planet.business.hibernate;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.apache.roller.planet.business.DatabaseProvider;
import org.apache.roller.planet.business.Planet;
import org.apache.roller.planet.business.PlanetDatabaseProvider;
import org.apache.roller.planet.business.PlanetManager;
import org.apache.roller.planet.business.PropertiesManager;


/**
 * Guice module for configuring Hibernate as Roller-backend.
 */
public class PlanetModule implements Module {

    public void configure(Binder binder) {
        
        binder.bind(DatabaseProvider.class).to(PlanetDatabaseProvider.class);

        binder.bind(Planet.class).to(HibernatePlanetImpl.class);
        
        binder.bind(HibernatePersistenceStrategy.class); 
        
        binder.bind(PlanetManager.class).to(     HibernatePlanetManagerImpl.class);   
        binder.bind(PropertiesManager.class).to( HibernatePropertiesManagerImpl.class);   
    }    
}
