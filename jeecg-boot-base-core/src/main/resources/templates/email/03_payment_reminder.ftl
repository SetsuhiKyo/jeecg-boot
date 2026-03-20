<!DOCTYPE html>
<html>
<body>
<p>${greeting} ${customerName},</p>

<p>${mail_paymentReminder_line1}</p>

<p>
    <strong>${label_orderNo}:</strong> ${orderNo}<br/>
    <strong>${label_amount}:</strong> ${amount}<br/>
    <strong>${label_paymentDeadline}:</strong> ${paymentDeadline}
</p>

<p style="color:red;">
    ${mail_paymentReminder_warning}
</p>

<p>${mail_paymentReminder_line2}</p>

<br/>
<p>${signature_line1}</p>
<p>${signature_line2}</p>
</body>
</html>