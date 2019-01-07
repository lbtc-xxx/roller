package org.apache.roller.weblogger.ui.struts2.admin;

import org.apache.struts2.StrutsTestCase;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

public class SomeStrutsTestCase extends StrutsTestCase {
    /*
    in order to run a StrutsTestCase the following things should be done:

    1. set <spring.version>4.3.13.RELEASE</spring.version> (otherwise there will be MethodNotFoundError by StrutsTestCase)
    2. remove struts2-spring-plugin from dependencies
     */
    public void testName() {
        ActionMapping actionMapping = getActionMapping("/roller-ui/admin/globalConfig.action");
        System.out.println(actionMapping);
    }
}
