<?xml version="1.0" encoding="UTF-8"?>
<vxml version="2.0" xmlns="http://www.w3.org/2001/vxml" xml:lang="en-US" application="include/approot.xml">
<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.speechpro.biometric.platform.onepass.api.PersonApi"%>
<%@ page import="com.speechpro.biometric.platform.onepass.api.OnePassApi"%>
<%@ page import="com.speechpro.biometric.platform.onepass.api.VerificationApi"%>

<%response.setHeader("Cache-Control", "no-cache");%>

<%
	Logger log = Logger.getLogger("getPerson");
	log.info("=====verificationResult.jsp=====");
	request.setCharacterEncoding("UTF-8");

	String sessionId = (request.getParameter("sessionId") != null && !request.getParameter("sessionId").isEmpty())
			? request.getParameter("sessionId") : "02";
	String personId = (request.getParameter("personId") != null && !request.getParameter("personId").isEmpty())
			? request.getParameter("personId") : "02";
	String root = (request.getParameter("root") != null
          	&& !request.getParameter("root").isEmpty()) ? request.getParameter("root") : "none";
  	String protocol = (request.getParameter("protocol") != null
    && !request.getParameter("protocol").isEmpty()) ? request.getParameter("protocol") : "none";
  	String host = (request.getParameter("host") != null
    && !request.getParameter("host").isEmpty()) ? request.getParameter("host") : "none";
   	String port = (request.getParameter("port") != null
    && !request.getParameter("port").isEmpty()) ? request.getParameter("port") : "none";

	log.info("      jsp: verificationSessionId = " + sessionId);
	log.info("      jsp: personId = " + personId);
	log.info("		jsp: root = " + root);
	log.info("		jsp: protocol = " + protocol);
   	log.info("		jsp: port = " + port);
   	log.info("		jsp: host = " + host);


	OnePassApi onePassApi 	= new OnePassApi(protocol, host, port, root);
    PersonApi personApi 	= onePassApi.person(personId);
    VerificationApi verificationApi = personApi.verification(sessionId);

    int score = Double.valueOf(verificationApi.getVerificationScore()).intValue();
	boolean closed = verificationApi.closeVerificationSession();
    log.info("      jsp: score = " + score);

%>

<form id="getPerson">
	<block>
		<var name="score" expr="'<%=score%>'"/>
		<return namelist="score"/>
	</block>
	</form>
</vxml>