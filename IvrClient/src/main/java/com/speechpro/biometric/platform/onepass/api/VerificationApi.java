package com.speechpro.biometric.platform.onepass.api;

import com.speechpro.biometric.platform.onepass.dto.*;
import com.speechpro.biometric.platform.onepass.exceptions.ExceptionMapper;
import com.speechpro.biometric.platform.onepass.rest.OnePassRestClient;
import com.speechpro.biometric.platform.onepass.util.DataConverter;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by sadurtinova on 15.09.2016.
 */
public class VerificationApi {
    private static final Logger LOGGER = Logger.getLogger("PersonApi");

    private final UUID sessionId;
    private final String personId;
    private UUID transactionId;
    private String verificationPassword = null;

    /**
     * Create object with verification methods for a particular session and particular person
     * @param personId person identifier
     * @param sessionId session identifier
     */
    public VerificationApi(String personId, UUID sessionId) {
        this.sessionId = sessionId;
        this.personId = personId;
        getVerificationId();
        LOGGER.info(String.format("VerificationApi initialized. SessionId = %s",
                sessionId));
    }

    /**
     * Create object with verification methods for a particular session and particular person
     * @param personId person identifier
     * @param sessionId session identifier
     * @param transactionId transaction identifier
     */
    public VerificationApi(String personId, UUID sessionId, UUID transactionId){
        this.sessionId = sessionId;
        this.personId = personId;
        this.transactionId = transactionId;
    }

    /**
     * Gets session identifier
     * @return session identifier
     */
    public UUID getSessionId() {
        return sessionId;
    }

    public UUID getTransactionId(){return transactionId;}

    /**
     * Starts verification transaction to get transaction identifier and password to be read for verification
     */
    private void getVerificationId() {
        try (CloseableHttpResponse response = OnePassRestClient.get().startVerification(personId,
                new Header[]{new BasicHeader("X-Session-Id", sessionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                StartDynamicVerificationRequestDto verification = DtoHelper.create(response.getEntity().getContent(),
                        StartDynamicVerificationRequestDto.class);
                this.transactionId = UUID.fromString(verification.transactionId);
                this.verificationPassword = verification.password;
                LOGGER.info("Password is" + verificationPassword);
            } else {
                ExceptionMapper.map(response);
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't start startVerification session: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets verification password
     * @return verification password to be read by person
     */
    public String getVerificationPassword() {
        return verificationPassword;
    }

    /**
     * Send audio file containing record with pronounced dynamic verification password generated by biometric system,
     * this method is used for text dependent verification
     * @param file sound record with pronounced verification password
     * @return true if sound data is successfully processed
     */
    public boolean sendDynamicVerificationVoice(File file) {
        boolean sent = false;
        try (CloseableHttpResponse response =
                     OnePassRestClient.get().sendVerificationVoiceDynamicFile(
                             sessionId.toString(),
                             new SendDynamicFileRequestDto(verificationPassword, DataConverter.convertFileToBase64(file)),
                             new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                                     new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                sent = true;
                LOGGER.info(String.format("Dynamic startVerification data sent in session %s", sessionId));
            } else {
                ExceptionMapper.map(response);
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't send startVerification sound data for session with id: " + sessionId);
            e.printStackTrace();
        }
        return sent;
    }

    /**
     * Send audio file containing record with pronounced dynamic verification password generated by biometric system,
     * this method is used for text dependent verification
     * @param data sound record with pronounced verification password converted to Base64 string
     * @return true if sound data is successfully processed
     */
    public boolean sendDynamicVerificationVoice(String data) {
        boolean sent = false;
        try (CloseableHttpResponse response =
                     OnePassRestClient.get().sendVerificationVoiceDynamicFile(
                             sessionId.toString(),
                             new SendDynamicFileRequestDto(verificationPassword, data),
                             new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                                     new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                sent = true;
                LOGGER.info(String.format("Dynamic startVerification data sent in session %s", sessionId));
            } else {
                ExceptionMapper.map(response);
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't send startVerification sound data for session with id: " + sessionId);
            e.printStackTrace();
        }
        return sent;
    }

    /**
     * Send audio file containing record with pronounced dynamiv verification password generated by biometric system,
     * this method is used for text dependent verification
     * @param data sound record with pronounced verification password converted to Base64 string
     * @param verificationPassword verification password
     * @return true if sound data is successfully processed
     */
    public boolean sendDynamicVerificationVoice(String verificationPassword, String data) {
        boolean sent = false;
        try (CloseableHttpResponse response =
                     OnePassRestClient.get().sendVerificationVoiceDynamicFile(
                             sessionId.toString(),
                             new SendDynamicFileRequestDto(verificationPassword, data),
                             new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                                     new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                sent = true;
                LOGGER.info(String.format("Dynamic startVerification data sent in session %s", sessionId));
            } else {
                ExceptionMapper.map(response);
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't send startVerification sound data for session with id: " + sessionId);
            e.printStackTrace();
        }
        return sent;
    }

    /**
     * Send audio file containing record with pronounced static verification password,
     * this method is used for text independent verification
     * @param file sound record with pronounced verification password
     * @return true if sound data is successfully processed
     */
    public boolean sendStaticVerificationVoice(File file) {
        boolean sent = false;
        try (CloseableHttpResponse response =
                     OnePassRestClient.get().sendVerificationVoiceStaticFile(
                             sessionId.toString(),
                             new SendStaticFileRequestDto(DataConverter.convertFileToBase64(file)),
                             new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                                     new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                sent = true;
                LOGGER.info(String.format("Static startVerification voice data sent in session %s", sessionId));
            } else {
                ExceptionMapper.map(response);
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't send startVerification sound data for session with id: " + sessionId);
            e.printStackTrace();
        }
        return sent;
    }

    /**
     * Send audio file containing record with pronounced static verification password,
     * this method is used for text independent verification
     * @param base64Data sound record with pronounced verification password converted to base64 string
     * @return true if sound data is successfully processed
     */
    public boolean sendStaticVerificationVoice(String base64Data) {
        boolean sent = false;
        try (CloseableHttpResponse response =
                     OnePassRestClient.get().sendVerificationVoiceStaticFile(
                             sessionId.toString(),
                             new SendStaticFileRequestDto(base64Data),
                             new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                                     new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                sent = true;
                LOGGER.info(String.format("Static startVerification voice data sent in session %s", sessionId));
            } else {
                ExceptionMapper.map(response);
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't send startVerification sound data for session with id: " + sessionId);
            e.printStackTrace();
        }
        return sent;
    }

    /**
     * Send file containing person's photo
     * @param file person's photo
     * @return true if photo is successfully processed
     */
    public boolean sendVerificationPhoto(File file) {
        boolean sent = false;
        try (CloseableHttpResponse response =
                     OnePassRestClient.get().sendPersonPhotoFile(
                             new SendFaceFileRequestDto(DataConverter.convertFileToBase64(file)),
                             new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                                     new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                sent = true;
                LOGGER.info(String.format("Static startVerification photo data sent in session %s", sessionId));
            } else {
                ExceptionMapper.map(response);
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't send startVerification sound data for session with id: " + sessionId);
            e.printStackTrace();
        }
        return sent;
    }

    /**
     * Gets verification score
     * @return verification score
     */
    public double getDynamicVerificationScore() {
        double result = 0;
        try (CloseableHttpResponse response = OnePassRestClient.get().getVerificationScore(sessionId.toString(),
                new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                        new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                GetDynamicVerificationResultResponseDto verificationScore =
                        DtoHelper.create(response.getEntity().getContent(),
                                GetDynamicVerificationResultResponseDto.class);
                result = verificationScore.score;
            } else {
                ExceptionMapper.map(response);
            }
            LOGGER.info("getDynamicVerificationScore() method called, result is : " + result);
        } catch (IOException e) {
            LOGGER.error("Couldn't get startVerification result for session with id: " + sessionId);
            e.printStackTrace();        }
        return result;
    }

    public double getStaticVerificationScore() {
        double result = 0;
        try (CloseableHttpResponse response = OnePassRestClient.get().getVerificationScore(sessionId.toString(),
                new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                        new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                GetStaticVerificationResultResponseDto verificationScore =
                        DtoHelper.create(response.getEntity().getContent(),
                                GetStaticVerificationResultResponseDto.class);
                result = verificationScore.score;
            } else {
                ExceptionMapper.map(response);
            }
            LOGGER.info("getStaticVerificationScore() method called, result is : " + result);
        } catch (IOException e) {
            LOGGER.error("Couldn't get startVerification result for session with id: " + sessionId);
            e.printStackTrace();
        }
        return result;
    }

    public boolean closeVerificationSession() {
        boolean closed = false;
        try (CloseableHttpResponse response = OnePassRestClient.get().closeVerificationSession(sessionId.toString(),
                new Header[]{new BasicHeader("X-Session-Id", sessionId.toString()),
                        new BasicHeader("X-Transaction-Id", transactionId.toString())})) {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                closed = true;
                LOGGER.info("closeVerificationSession() method called, result is : " + closed);
            } else {
                ExceptionMapper.map(response);
            }
        } catch (IOException e) {
            LOGGER.error("Couldn't close startVerification session with id: " + sessionId);
            e.printStackTrace();
        }
        return closed;
    }
}
