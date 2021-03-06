/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */

package org.apache.roller.weblogger.pojos;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.roller.util.UUIDGenerator;


/**
 * Automatic ping configuration.  An instance of this class relates a website 
 * and ping target; it indicates that the specified ping target should be pinged
 * when the corresponding website is changed.
 *
 * @author <a href="mailto:anil@busybuddha.org">Anil Gangolli</a>
 */
public class AutoPing implements Serializable {

    private String id = UUIDGenerator.generateUUID();
    private PingTarget pingTarget = null;
    private Weblog website = null;

    public static final long serialVersionUID = -9105985454111986435L;
    
    
    /**
     * Default constructor leaves all fields null. Required for bean compliance.
     */
    public AutoPing() {
    }

    /**
     * Constructor.
     * @param id         unique id (primary key) for this instance
     * @param pingtarget ping target that should be pinged
     * @param website    website to which this configuration applies
     */
    public AutoPing(String id, PingTarget pingtarget, Weblog website) {
        //this.id = id;
        this.website = website;
        this.pingTarget = pingtarget;
    }

    /**
     * Get the unique id (primary key) of this object.
     * @return the unique id of this object. 
     */
    public String getId() {
        return id;
    }

    /**
     * Set the unique id (primary key) of this object
     * @param id
     */
    public void setId(String id) {
        // Form bean workaround: empty string is never a valid id
        if (id != null && id.trim().length() == 0) {
            return;
        }
        this.id = id;
    }

    /**
     * Get the website.  Get the website whose changes should result in a ping 
     * to the ping target specified by this object.
     * @return the website.
     */
    public Weblog getWebsite() {
        return website;
    }

    /**
     * Set the website.  Set the website whose changes should result in a ping 
     * to the ping target specified by this object.
     * @param website the website.
     */
    public void setWebsite(Weblog website) {
        this.website = website;
    }

    /**
     * Get the ping target. Get the target to be pinged when the
     * corresponding website changes.
     * @return the target to be pinged.
     */
    public PingTarget getPingTarget() {
        return pingTarget;
    }

    /**
     * Set the ping target.  Set the target to be pinged when the
     * corresponding website changes.
     * @param pingtarget the target to be pinged.
     */
    public void setPingTarget(PingTarget pingtarget) {
        this.pingTarget = pingtarget;
    }

    //------------------------------------------------------- Good citizenship

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("{");
        buf.append(getId());
        buf.append("}");
        return buf.toString();
    }
    
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AutoPing)) {
            return false;
        }
        AutoPing o = (AutoPing)other;
        return new EqualsBuilder()
            .append(getId(), o.getId())
            .append(getPingTarget(), o.getPingTarget()) 
            .append(getWebsite(), o.getWebsite()) 
            .isEquals();
    }
    
    public int hashCode() { 
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
    
}
