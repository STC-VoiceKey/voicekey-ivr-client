package com.speechpro.biometric.platform.onepass.rest;

import com.speechpro.biometric.platform.onepass.dto.CreatePersonRequestDto;
import com.speechpro.biometric.platform.onepass.dto.SendDynamicFileRequestDto;
import com.speechpro.biometric.platform.onepass.dto.SendStaticFileRequestDto;
import com.speechpro.biometric.platform.onepass.exceptions.NotInitializedException;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;

import java.util.Collections;

/**
 * Created by sadurtinova on 09.09.2016.
 */
public class OnePassRestClient {

    private static OnePassRestClient instance;

    public static void initialize(String protocol, String host, String port, String applicationRoot){
        if (instance == null){
            instance = new OnePassRestClient(protocol, host, port, applicationRoot);
        }
    }

    public static void release(){
        if(instance != null){
            instance.releaseInternal();
        }
    }

    public static OnePassRestClient get(){
        if(instance != null) //here you can init Client
            return instance;
        else
            throw new NotInitializedException("OnePass REST API client is not initialized. Call initialize() method first");

}

    private BasicRestClient client;
    private OnePassRestPaths paths;

    private OnePassRestClient(String protocol, String host, String port, String applicationRoot){
    client = new BasicRestClient(Collections.<Header>singletonList(
                    new BasicHeader("Content-Type", "application/json; charset=UTF-8")
                    ));
    paths = new OnePassRestPaths(protocol, host, port, applicationRoot);
}

    public CloseableHttpResponse sendPersonVoiceDynamicFile(String personId, SendDynamicFileRequestDto data){
        return client.post(paths.getPersonVoiceDynamicFileUri(personId), data);
}

    public CloseableHttpResponse sendPersonVoiceStaticFile(String personId, SendStaticFileRequestDto data){
        return client.post(paths.getPersonVoiceStaticFileUri(personId), data);
}

    public CloseableHttpResponse sendVerificationVoiceDynamicFile (String sessionId, SendDynamicFileRequestDto data){
        return client.post(paths.getVerificationVoiceDynamicFileUri(sessionId), data);
}

    public CloseableHttpResponse sendVerificationVoiceStaticFile (String personId, SendStaticFileRequestDto data){
        return client.post(paths.getVerificationVoiceStaticFileUri(personId), data);
}

    public CloseableHttpResponse getPerson (String personId){
        return client.get(paths.getPersonUri(personId));
}

    public CloseableHttpResponse deletePerson (String personId){
        return client.delete(paths.getPersonUri(personId));
}

    public CloseableHttpResponse startVerification(String personId){
        return client.get(paths.getVerificationStartUri(personId));
}

    public CloseableHttpResponse getVerificationScore (String sessionID) {
        return client.get(paths.getVerificationScoreCloseSessionFalseUri(sessionID));
    }

    public CloseableHttpResponse createPerson(CreatePersonRequestDto data) {
        return client.post(paths.getCreatePersonUri(), data);
    }

    public CloseableHttpResponse closeVerificationSession (String sessionId){
        return client.delete(paths.getCloseVerificationUri(sessionId));
    }

    public void releaseInternal(){
        client.release();
    }

}

