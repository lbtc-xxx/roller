<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  The ASF licenses this file to You
  under the Apache License, Version 2.0 (the "License"); you may not
  use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.  For additional information regarding
  copyright in this work, please see the NOTICE file in the top level
  directory of this distribution.
-->
<%@ include file="/WEB-INF/jsps/taglibs.jsp" %>

<h2><fmt:message key="welcome.title" /></h2>

<% 

	String activationStatus = (String) request.getAttribute("activationStatus"); 
	if (activationStatus != null){
		if (activationStatus.equals("0")) {
			%>
			<p><fmt:message key="welcome.accountCreated" /></p>
			<p><fmt:message key="welcome.user.account.not.activated" /></p>
			<% 
		} else if(activationStatus.equals("1")) {
			%>			
			<p><fmt:message key="welcome.user.account.activated" /></p>
			<p><html:link forward="login-redirect"><fmt:message key="welcome.clickHere" /></html:link> 
			<fmt:message key="welcome.toLoginAndPost" /></p>
			<%
		} else if(activationStatus.equals("-1")) {
			//error
		}
	} else {
		%>
		<p><fmt:message key="welcome.accountCreated" /></p>
		<p><html:link forward="login-redirect"><fmt:message key="welcome.clickHere" /></html:link> 
		<fmt:message key="welcome.toLoginAndPost" /></p>
		<% 
	}

%>


<br />
<br />
<br />