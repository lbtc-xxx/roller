/*
 * Copyright 2005 Sun Microsystems, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.roller.pojos;

import java.io.Serializable;

import org.roller.pojos.PersistentObject;

/**
 * @hibernate.class table="rag_group_subscription"
 * @author Dave Johnson
 */
public class PlanetGroupSubscriptionAssoc extends PersistentObject 
        implements Serializable
{
    /** Database ID */
    protected String id;
    
    protected PlanetGroupData group;
    protected PlanetSubscriptionData subscription;

    //----------------------------------------------------------- persistent fields
    /** 
     * @hibernate.id column="id" type="string" 
     *     generator-class="uuid.hex" unsaved-value="null"
     */
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    /** 
     * @hibernate.many-to-one column="group_id" cascade="none" not-null="false"
     */
    public PlanetGroupData getGroup()
    {
        return group;
    }
    public void setGroup(PlanetGroupData group)
    {
        this.group = group;
    }
    
    /** 
     * @hibernate.many-to-one column="subscription_id" cascade="none" not-null="false"
     */
    public PlanetSubscriptionData getSubscription()
    {
        return subscription;
    }    
    public void setSubscription(PlanetSubscriptionData subscription)
    {
        this.subscription = subscription;
    }

    //-------------------------------------------------------------- implementation
    public void setData(PersistentObject vo)
    {
    }
}