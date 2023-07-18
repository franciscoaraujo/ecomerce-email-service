package br.com.fiaptcc.msemailservice.aws.service;

import br.com.fiaptcc.msemailservice.email.model.dto.EmailDto;

import br.com.fiaptcc.msemailservice.email.model.dto.EmailMSgDto;
import br.com.fiaptcc.msemailservice.email.service.EmailService;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageQueueService {

    @Value("${from.email}")
    private String fromEmail;

    @Value("${message.standard}")
    private String standardReponse;

    @Value("${cloud.aws.end-point.uri}")
    private String messageQueueTopic;

    private final AmazonSQS amazonSQSClient;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedDelay = 20000) //executes on every 20 second gap.
    public void receiveMessages() {
        try {
            String queueUrl = amazonSQSClient.getQueueUrl(messageQueueTopic).getQueueUrl();
            log.info("Reading SQS Queue done: URL {}", queueUrl);

            ReceiveMessageResult receiveMessageResult = amazonSQSClient.receiveMessage(queueUrl);

            if (!receiveMessageResult.getMessages().isEmpty()) {
                Message message = receiveMessageResult.getMessages().get(0);
                log.info("Incoming Message From SQS {}", message.getMessageId());
                log.info("Message Body {}", message.getBody());
                processInvoice(message.getBody());
                amazonSQSClient.deleteMessage(queueUrl, message.getReceiptHandle());
            }

        } catch (QueueDoesNotExistException e) {
            log.error("Queue does not exist {}", e.getMessage());
        }
    }

    private void processInvoice(String body) {
        log.info("Starting to send the e-mail...");
        Gson gson = new Gson();
        EmailMSgDto emailMSgDto = gson.fromJson(body, EmailMSgDto.class);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromEmail);
        simpleMailMessage.setTo(emailMSgDto.getEmail());
        simpleMailMessage.setSubject(standardReponse + ": " + emailMSgDto.getRequestNumber());
        simpleMailMessage.setText(emailMSgDto.getMessage());
        emailService.sendMessage(simpleMailMessage);

        log.info("Email sent successfully...");
    }
}
