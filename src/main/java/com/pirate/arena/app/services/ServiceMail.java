package com.pirate.arena.app.services;

import com.amazonaws.services.dynamodbv2.model.InternalServerErrorException;
import com.pirate.arena.app.requests.RequestSendMail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;


@Service
@RequiredArgsConstructor
public class ServiceMail {


    private String getHTMLBody(String code) {
        StringBuilder body = new StringBuilder("<html><body><div style=\"justify-content: center; width: 95%\"><h1 style=\"text-transform: uppercase; text-align: center\"><img src=\"https://onepice-arena-resources.s3.amazonaws.com/website/icon.png\"style=\"width: 40px;  margin-right: 10px;\" />Welcome to <br> One Piece Arena</h1><p style=\"text-align: center\">Yor validation code is:</p><div style=\"text-align: center; margin-bottom: 20px;\">");
        for (String number : code.split("")) {
            body.append("<span style=\"font-size: 30px; padding: 9px; border: 1px solid black; text-align: center; margin: 1px; border-radius: 5px; background-color: rgba(209, 212, 212, 0.207);\">").append(number).append("</span>");
        }
        body.append("</div></div></body></html>");
        return body.toString();
    }

    public void sendWelcomeMail(RequestSendMail user, String code) {
        try {
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1).build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(user.emailReceiver()))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(getHTMLBody(code))))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData("Welcome "
                                                    .concat(" to One Piece Arena!!!"))))
                    .withSource("no-replay@onepiece-arena.com");
            client.sendEmail(request);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalServerErrorException("Error: " + ex.getMessage());
        }
    }

}
