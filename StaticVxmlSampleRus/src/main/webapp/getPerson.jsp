<?xml version="1.0" encoding="UTF-8"?>
<vxml version="2.0" xmlns="http://www.w3.org/2001/vxml" xml:lang="ru-RU" application="include/approot.xml">
    <%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.speechpro.biometric.platform.onepass.api.OnePassApi" %>
    <%@ page import="com.speechpro.biometric.platform.onepass.api.PersonApi" %>
    <%@ page import="com.speechpro.biometric.platform.onepass.api.RegistrationApi" %>
    <%@ page import="com.speechpro.biometric.platform.onepass.api.SessionApi" %>
    <%@ page import="org.apache.log4j.Logger" %>
    <%@ page import="java.util.UUID" %>
    <%response.setHeader("Cache-Control", "no-cache");%>

    <%

        Logger log = Logger.getLogger("getPerson");
        request.setCharacterEncoding("UTF-8");
        log.info("=====getPerson.jsp=====");
        String personId = (request.getParameter("personId") != null
                && !request.getParameter("personId").isEmpty()) ? request.getParameter("personId") : "00";
        personId = personId.replaceAll(" ", "");
        personId = personId.replaceAll("'", "");
        personId = personId.replaceAll(";", "");

        String host = (request.getParameter("host") != null
                && !request.getParameter("host").isEmpty()) ? request.getParameter("host") : "00";
        String port = (request.getParameter("port") != null
                && !request.getParameter("port").isEmpty()) ? request.getParameter("port") : "";
        String root = (request.getParameter("root") != null
                && !request.getParameter("root").isEmpty()) ? request.getParameter("root") : "none";
        String protocol = (request.getParameter("protocol") != null
                && !request.getParameter("protocol").isEmpty()) ? request.getParameter("protocol") : "none";

        log.info("      jsp: personId: " + personId);
        log.info("      jsp: host: " + host);
        log.info("      jsp: port: " + port);
        log.info("      jsp: root: " + root);
        log.info("      jsp: protocol: " + protocol);

        OnePassApi onePassApi = new OnePassApi(protocol, host, port, root);
        SessionApi sessionApi = new SessionApi("ivr_user", "QL0AFWMIX8NRZTKeof9cXsvbvu8=", 201);
        UUID sessionId = sessionApi.startSession();
        PersonApi personApi = onePassApi.person(personId, sessionId);
        RegistrationApi registrationApi = personApi.startRegistration();
        boolean createdPerson = false;
        boolean personExists = personApi.personExists();
        UUID registrationTransactionSession = UUID.randomUUID();
        if (personExists) {
            boolean isFullyEnrolled = personApi.isFullyEnrolled();
            log.info("      jsp: isFullEnrollment: " + isFullyEnrolled);
            if (!isFullyEnrolled) {
                personApi.deletePerson();
                registrationApi.createPerson();
                registrationTransactionSession = registrationApi.getTransactionId();
            }
        } else if (!personExists) {
            registrationApi.createPerson();
            registrationTransactionSession = registrationApi.getTransactionId();
        }

        log.info("      jsp: personExists: " + personExists);
        log.info("      jsp: createdPerson: " + createdPerson);
    %>

    <form id="getPerson">
        <block>
            <assign name="application.sessionId" expr="'<%=sessionId.toString()%>'"/>
            <assign name="application.personId" expr="'<%=personId%>'"/>
            <if cond="<%=personExists%> == true">
                <submit next="verification.xml" namelist="personId sessionId transactionId"/>
                <else/>
                <assign name="application.transactionId" expr="'<%=registrationTransactionSession.toString()%>'"/>
                <goto next="enrollment_1.xml"/>
            </if>
        </block>
    </form>
</vxml>