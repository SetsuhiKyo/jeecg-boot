package org.jeecg.modules.message.handle.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.modules.message.handle.ISendEmailHandle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description: 邮箱发送信息
 * @author: jeecg-boot
 */
@Slf4j
@Component("emailSendMsgHandle")
@Profile("prod")
public class SendGridSendEmailHandle implements ISendEmailHandle {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;


    @Override
    public void sendMsg(String to, String subject, String contentStr) {
        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", contentStr);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        // 未使用
    }

}
