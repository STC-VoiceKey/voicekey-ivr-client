<?xml version="1.0" encoding="UTF-8"?>
<vxml version="2.0" xmlns="http://www.w3.org/2001/vxml" xml:lang="ru-RU" application="include/approot.xml">
	<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import="org.apache.log4j.Logger" %>
	<%@ page import="com.speechpro.biometric.platform.onepass.util.ConfigReader"%>
	<%response.setHeader("Cache-Control", "no-cache");%>

	<%

		Logger log = Logger.getLogger("params");
		request.setCharacterEncoding("UTF-8");
		log.info("=====params.jsp=====");

		ConfigReader cr = new ConfigReader();
		String host 		= cr.readProperty("biometric.platform.host");
		String port 		= cr.readProperty("biometric.platform.port");
		String threshold	= cr.readProperty("biometric.platform.threshold");
		String attempts 	= cr.readProperty("biometric.platform.attempts");
	    String root 		= cr.readProperty("biometric.platform.root");
	    String protocol 	= cr.readProperty("biometric.platform.protocol");

		log.info("      jsp: host: " + host);
		log.info("      jsp: port: " + port);
		log.info("      jsp: threshold: " + threshold);
		log.info("      jsp: attempts: " + attempts);
        log.info("      jsp: root: " + root);
        log.info("      jsp: protocol: " + protocol);

	%>

	<form id="getPerson">
		<block>
			<assign name = "application.host" 		expr = "'<%=host%>'"/>
			<assign name = "application.port" 		expr = "<%=port%>"/>
			<assign name = "application.threshold" 	expr = "<%=threshold%>"/>
			<assign name = "application.attempts" 	expr = "<%=attempts%>"/>
			<assign name = "application.root"       expr = "'<%=root%>'"/>
			<assign name = "application.protocol"   expr = "'<%=protocol%>'"/>
			<goto next="hello.xml"/>
		</block>
	</form>
</vxml>