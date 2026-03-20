<!DOCTYPE html>
<html>
<body>
<p>${greeting} ${customerName},</p>

<p>${mail_orderCreated_line1}</p>

<p>
    <strong>${label_orderNo}:</strong> ${orderNo}<br/>
    <strong>${label_serviceDate}:</strong> ${serviceDate}
</p>

<p>${mail_orderCreated_line2}</p>

<div style="text-align:center; margin: 30px 0;">
    <a href="${orderUrl}"
       style="background-color: #007bff; color: #ffffff; padding: 12px 25px;
                  text-decoration: none; border-radius: 5px;">
        ${buttonText}
    </a>
</div>
<br/>
<p>${signature_line1}</p>
<p>${signature_line2}</p>
</body>
</html>