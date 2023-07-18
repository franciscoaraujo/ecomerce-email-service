package br.com.fiaptcc.msemailservice.aws.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MessageQueueServiceTest {

    @Test
    void testReceiveMessages2() {

        AmazonSQSAsyncClient amazonSQSClient = mock(AmazonSQSAsyncClient.class);
        when(amazonSQSClient.receiveMessage(Mockito.<String>any())).thenReturn(new ReceiveMessageResult());
        when(amazonSQSClient.getQueueUrl(Mockito.<String>any())).thenReturn(new GetQueueUrlResult());
        (new MessageQueueService(amazonSQSClient)).receiveMessages();
        verify(amazonSQSClient).getQueueUrl(Mockito.<String>any());
        verify(amazonSQSClient).receiveMessage(Mockito.<String>any());
    }

    /**
     * Method under test: {@link MessageQueueService#receiveMessages()}
     */
    @Test
    void testReceiveMessages3() {

        AmazonSQSAsyncClient amazonSQSClient = mock(AmazonSQSAsyncClient.class);
        when(amazonSQSClient.receiveMessage(Mockito.<String>any()))
                .thenThrow(new QueueDoesNotExistException("An error occurred"));
        when(amazonSQSClient.getQueueUrl(Mockito.<String>any())).thenReturn(new GetQueueUrlResult());
        (new MessageQueueService(amazonSQSClient)).receiveMessages();
        verify(amazonSQSClient).getQueueUrl(Mockito.<String>any());
        verify(amazonSQSClient).receiveMessage(Mockito.<String>any());
    }
   @Test
    void testReceiveMessages5() {
        ReceiveMessageResult receiveMessageResult = mock(ReceiveMessageResult.class);
        when(receiveMessageResult.getMessages()).thenThrow(new QueueDoesNotExistException("An error occurred"));
        AmazonSQSAsyncClient amazonSQSClient = mock(AmazonSQSAsyncClient.class);
        when(amazonSQSClient.receiveMessage(Mockito.<String>any())).thenReturn(receiveMessageResult);
        when(amazonSQSClient.getQueueUrl(Mockito.<String>any())).thenReturn(new GetQueueUrlResult());
        (new MessageQueueService(amazonSQSClient)).receiveMessages();
        verify(amazonSQSClient).getQueueUrl(Mockito.<String>any());
        verify(amazonSQSClient).receiveMessage(Mockito.<String>any());
        verify(receiveMessageResult).getMessages();
    }

    @Test
    void testReceiveMessages7() {
        GetQueueUrlResult getQueueUrlResult = mock(GetQueueUrlResult.class);
        when(getQueueUrlResult.getQueueUrl()).thenThrow(new QueueDoesNotExistException("An error occurred"));
        AmazonSQSAsyncClient amazonSQSClient = mock(AmazonSQSAsyncClient.class);
        when(amazonSQSClient.getQueueUrl(Mockito.<String>any())).thenReturn(getQueueUrlResult);
        (new MessageQueueService(amazonSQSClient)).receiveMessages();
        verify(amazonSQSClient).getQueueUrl(Mockito.<String>any());
        verify(getQueueUrlResult).getQueueUrl();
    }
}

