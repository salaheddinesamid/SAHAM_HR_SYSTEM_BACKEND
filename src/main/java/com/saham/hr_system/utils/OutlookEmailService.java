package com.saham.hr_system.utils;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutlookEmailService {

    private final GraphServiceClient<Request> graphClient;
    private final String fromAddress;

    public OutlookEmailService(
            @Value("${outlook.client-id}") String clientId,
            @Value("${outlook.tenant-id}") String tenantId,
            @Value("${outlook.client-secret}") String clientSecret,
            @Value("${outlook.user-email}") String fromAddress
    ) {
        this.fromAddress = fromAddress;

        ClientSecretCredential credential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();

        graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(new TokenCredentialAuthProvider(
                        List.of("https://graph.microsoft.com/.default"), credential))
                .buildClient();
    }

    public void sendEmail(
            String to, String htmlBody, String subject
    ){
        Message message = new Message();
        message.subject = subject;

        ItemBody body = new ItemBody();
        body.contentType = BodyType.HTML;
        body.content = htmlBody;
        message.body = body;

        Recipient recipient = new Recipient();
        EmailAddress address = new EmailAddress();
        address.address = to;
        recipient.emailAddress = address;

        message.toRecipients = List.of(recipient);

        graphClient.users(fromAddress)
                .sendMail(UserSendMailParameterSet
                        .newBuilder()
                        .withMessage(message)
                        .withSaveToSentItems(true)
                        .build())
                .buildRequest()
                .post();
    }
}
