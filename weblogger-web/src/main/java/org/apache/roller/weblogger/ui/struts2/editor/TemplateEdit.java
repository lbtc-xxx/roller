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

package org.apache.roller.weblogger.ui.struts2.editor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.roller.weblogger.WebloggerException;
import org.apache.roller.weblogger.business.WebloggerFactory;
import org.apache.roller.weblogger.config.WebloggerConfig;
import org.apache.roller.weblogger.pojos.WeblogPermission;
import org.apache.roller.weblogger.pojos.WeblogTemplate;
import org.apache.roller.weblogger.ui.struts2.util.UIAction;
import org.apache.roller.weblogger.util.Utilities;
import org.apache.roller.weblogger.util.cache.CacheManager;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Action which handles editing for a single WeblogTemplate.
 */
public class TemplateEdit extends UIAction {
    
    private static Log log = LogFactory.getLog(TemplateEdit.class);
    
    // form bean for collection all template properties
    private TemplateEditBean bean = new TemplateEditBean();
    
    // the template we are working on
    private WeblogTemplate template = null;

    private boolean mobileTemplateAvailable = false;
    
    
    public TemplateEdit() {
        this.actionName = "templateEdit";
        this.desiredMenu = "editor";
        this.pageTitle = "pagesForm.title";
    }
    
    
    @Override
    public List<String> requiredWeblogPermissionActions() {
        return Collections.singletonList(WeblogPermission.ADMIN);
    }
    

    public void myPrepare() {
        try {
            setTemplate(WebloggerFactory.getWeblogger().getWeblogManager().getPage(getBean().getId()));
            getBean().setStandardTemplateId(getStandardID());
            getBean().setMobileTemplateId(getMobileID());
            setMobileTemplateAvailable(hasManyTemplates());
        } catch (WebloggerException ex) {
            log.error("Error looking up template - "+getBean().getId(), ex);
        }
    }
    
    
    /**
     * Show template edit page.
     */
    @SkipValidation
    public String execute() {
        
        if(getTemplate() == null) {
            // TODO: i18n
            addError("Unable to locate specified template");
            return LIST;
        }
        
        WeblogTemplate page = getTemplate();
        getBean().copyFrom(template);


        // empty content-type indicates that page uses auto content-type detection
        if (StringUtils.isEmpty(page.getOutputContentType())) {
            getBean().setAutoContentType(Boolean.TRUE);
        } else {
            getBean().setAutoContentType(Boolean.FALSE);
            getBean().setManualContentType(page.getOutputContentType());
        }
        
        return INPUT;
    }

    /**
     * This is to sync mobile theme and standard theme if name or link changed
     */
      private void synchronizeThemes(){

          boolean isModified = false;
          WeblogTemplate mobileTemplate = null;
          WeblogTemplate standardTemplate = null;
          try {
              mobileTemplate = WebloggerFactory.getWeblogger().getWeblogManager().
                      getPage(getBean().getMobileTemplateId());
              standardTemplate = WebloggerFactory.getWeblogger().getWeblogManager().
                      getPage(getBean().getStandardTemplateId());
          } catch (WebloggerException e) {
              log.error("error in looking up theme ", e);
          }

          if (standardTemplate != null && mobileTemplate != null) {
              // if standard template has a different tempalte version we are going to change mobile accordingly
              if (!mobileTemplate.getName().equals(standardTemplate.getName() + ".Mobile")) {
                  mobileTemplate.setName(standardTemplate.getName() + ".Mobile");
                  isModified = true;
              }
              if (!mobileTemplate.getLink().equals(standardTemplate.getLink())) {
                  mobileTemplate.setLink(standardTemplate.getLink());
                  isModified = true;
              }
              if (isModified) {
                  // save template and flush
                  try {
                      WebloggerFactory.getWeblogger().getWeblogManager().savePage(mobileTemplate);
                      WebloggerFactory.getWeblogger().flush();
                  } catch (WebloggerException e) {
                      log.error("Error syncing with standard template", e);
                  }
                  // notify caches
                  CacheManager.invalidate(mobileTemplate);
              }

          }

      }
    
    
    /**
     * Save an existing template.
     */
    public String save() {
        
        if(getTemplate() == null) {
            // TODO: i18n
            addError("Unable to locate specified template");
            return LIST;
        }
        
        // validation
        myValidate();
        
        if(!hasActionErrors()) try {
            
            WeblogTemplate template = getTemplate();
            getBean().copyTo(template);
            template.setLastModified(new Date());
            
            if (getBean().getAutoContentType() == null ||
                    !getBean().getAutoContentType().booleanValue()) {
                template.setOutputContentType(getBean().getManualContentType());
            } else {
                // empty content-type indicates that template uses auto content-type detection
                template.setOutputContentType(null);
            }
            
            // save template and flush
            WebloggerFactory.getWeblogger().getWeblogManager().savePage(template);
            WebloggerFactory.getWeblogger().flush();
            
            // notify caches
            CacheManager.invalidate(template);

            synchronizeThemes();
            
            // success message
            addMessage("pageForm.save.success", template.getName());
            
        } catch (WebloggerException ex) {
            log.error("Error updating page - "+getBean().getId(), ex);
            // TODO: i18n
            addError("Error saving template");
        }
        
        return INPUT;
    }


    public String loadMobileTheme(){
         if(getTemplate() == null) {
            // TODO: i18n
            addError("Unable to locate specified template");
            return LIST;
        }

             WeblogTemplate mobile = null;

        try {
                mobile = WebloggerFactory.getWeblogger().getWeblogManager().getPageByName(getActionWeblog(),
                    template.getName()+".mobile");
        } catch (WebloggerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

         if(mobile == null){
            addError("Unable to locate specified template");
            return LIST;
         }
        getBean().copyFrom(mobile);


        // empty content-type indicates that page uses auto content-type detection
        if (StringUtils.isEmpty(mobile.getOutputContentType())) {
            getBean().setAutoContentType(Boolean.TRUE);
        } else {
            getBean().setAutoContentType(Boolean.FALSE);
            getBean().setManualContentType(mobile.getOutputContentType());
        }

        return INPUT;
    }


    
    private void myValidate() {
        
        // if name changed make sure there isn't a conflict
        if(!getTemplate().getName().equals(getBean().getName())) {
            try {
                if(WebloggerFactory.getWeblogger().getWeblogManager().getPageByName(getActionWeblog(), getBean().getName()) != null) {
                    addError("pagesForm.error.alreadyExists", getBean().getName());
                }
            } catch (WebloggerException ex) {
                log.error("Error checking page name uniqueness", ex);
            }
        }
         //TODO handle validation for link with combining type and link together
        // if link changed make sure there isn't a conflict
      /*  if(!StringUtils.isEmpty(getBean().getLink()) &&
                !getBean().getLink().equals(getTemplate().getLink())) {
            try {
                if(WebloggerFactory.getWeblogger().getWeblogManager().getPagesByLink(getActionWeblog(), getBean().getLink()) != null &&
                        ) {
                    addError("pagesForm.error.alreadyExists", getBean().getLink());
                }
            } catch (WebloggerException ex) {
                log.error("Error checking page link uniqueness", ex);
            }
        }*/
    }
    
    
    public List getTemplateLanguages() {
        String langs = WebloggerConfig.getProperty("rendering.templateLanguages","velocity");
        String[] langsArray = Utilities.stringToStringArray(langs, ",");
        return Arrays.asList(langsArray);
    }
    
    
    public TemplateEditBean getBean() {
        return bean;
    }

    public void setBean(TemplateEditBean bean) {
        this.bean = bean;
    }

    public WeblogTemplate getTemplate() {
        return template;
    }

    public void setTemplate(WeblogTemplate template) {
        this.template = template;


    }
       //get the ID for mobile tempalte
    private String getMobileID(){

        List<WeblogTemplate> templates = null;
        try {
            templates = WebloggerFactory.getWeblogger().getWeblogManager().
                    getPagesByLink(getActionWeblog(), template.getLink()) ;
        } catch (WebloggerException e) {
            log.error("error while getting template list." , e);
        }

        if (templates != null && templates.size() >1){
            for(WeblogTemplate template : templates){
                  if("mobile".equals(template.getType())){
                      return template.getId();
                  }
            }
        }
        return null;
    }

    public String getStandardID(){

        List<WeblogTemplate> templates = null;
        try {
            templates = WebloggerFactory.getWeblogger().getWeblogManager().
                    getPagesByLink(getActionWeblog(), template.getLink()) ;
        } catch (WebloggerException e) {
            log.error("error while getting template list." , e);
        }

        if (templates != null && !templates.isEmpty()){
            for(WeblogTemplate template : templates){
                  if("standard".equals(template.getType())){
                      return template.getId();
                  }
            }
        }
        return null;
    }

    public boolean hasManyTemplates() {

        List<WeblogTemplate> templates = null;
        try {
            templates = WebloggerFactory.getWeblogger().getWeblogManager().
                    getPagesByLink(getActionWeblog(), template.getLink());
        } catch (WebloggerException e) {
            log.error("error while getting template list.", e);
        }
        // we do not support editing mobile template pages for custom themes.
        if( "custom".equals(getActionWeblog().getEditorTheme())){
            return false;
        }
        else if (templates != null && templates.size() > 1) {
            return true;
        }
        return false;
    }

    public boolean isMobileTemplateAvailable() {
        return mobileTemplateAvailable;
    }

    public void setMobileTemplateAvailable(boolean mobileTemplateAvailable) {
        this.mobileTemplateAvailable = mobileTemplateAvailable;
    }
}