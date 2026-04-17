<!DOCTYPE html>
<html>
<body>
<p>${greeting} ${customerName}</p>

<p>${mail_orderConfirmed_line1}</p>

<p>
    <strong>${label_orderInfo}</strong><br/>
    <strong>${label_orderNo}:</strong> ${orderNo}<br/>
    <strong>${label_orderName}:</strong> ${orderName}<br/>
    <strong>${label_serviceDate}:</strong> ${serviceDate}<br/>
    <strong>${label_allamount}:</strong> ${allamount}${label_yen}<br/>
    <strong>${label_deposit}:</strong> ${deposit}${label_yen}<br/>
    <strong>${label_balance}:</strong> ${balance}${label_yen}<span>${label_balance_notes}</span><br/>
    <strong>${label_paymentDeadline}:</strong> ${paymentDeadline}
</p>
<p>
    <strong>${label_aboutPayment}</strong><br/>
    ${label_aboutPayment_line1}
    ${label_aboutPayment_line2}
</p>
<div style="font-size:13px; color:#d32f2f; font-weight:bold; padding-top:10px;"> ${label_aboutPayment_line3}</div>
<div style="font-size:15px; font-weight:bold;">
    ${label_aboutPayment_method1}
</div>
<div style="margin-top:8px;">
    👉 <a href="${label_aboutPayment_method1_linkurl}" style="color:#635bff; text-decoration:none; font-weight:bold;">
        ${label_aboutPayment_method1_linklabel}
    </a>
</div>

<div style="font-size:15px; font-weight:bold;">
    ${label_aboutPayment_method2}
</div>
<div style="margin-top:8px;">
    👉 <a href="${label_aboutPayment_method2_linkurl}" style="color:#635bff; text-decoration:none; font-weight:bold;">
        ${label_aboutPayment_method2_linklabel}
    </a>
</div>




<p>${mail_orderConfirmed_line2}</p>

<p>${mail_orderConfirmed_line3}</p>

<br/>
<p>${mail_footer_reminder}</p>
<!-- 分隔线 -->
<hr>

<!-- 邮件底部（重点） -->
<div style="font-size: 13px; color: #666;">

    <p><strong>${mail_footer_logo}</strong><br>
        ${mail_footer_logo_content}
    </p>

    <p>
        <strong>${mail_footer_support}</strong><br>
        💚 WhatsApp： <span><a href="${mail_footer_whatsapp_url}">${mail_footer_whatsapp_display}</a></span><br>
        💬 LINE： <span><a href="${mail_footer_line_url}">${mail_footer_line_display}</a></span><br>
        💬 微信： <span id="wechatId">${mail_footer_wechat_id}</span><button onclick="copyWeChat()">${mail_footer_wechat_copy}</button><br>
        📞 电话： <span><a href="tel:${mail_footer_phone_no}" style="text-decoration:none; color:#333;">${mail_footer_phone_display}</a></span><br>
        📧 邮箱： <span><a href="mailto:${mail_footer_email_url}">${mail_footer_email_display}</a></span><br>
        🌐 官网： <span><a href="${mail_footer_website_url}">${mail_footer_website_display}</a></span>
    </p>

    <p>
        <strong>${mail_footer_servicetime}</strong><br>
        ${mail_footer_servicetime_content}
    </p>

    <p>
        <strong>${mail_footer_servicerange}</strong><br>
        ${mail_footer_servicerange_content}
    </p>
    <!--
    <p>
        <strong>${mail_footer_companyinfo}</strong><br>
        ${mail_footer_companyinfo_name}<br>
        ${mail_footer_companyinfo_addr}<br>
    </p>
    -->
    <script>
        function copyWeChat() {
            const text = document.getElementById("wechatId").innerText;
            navigator.clipboard.writeText(text).then(() => {
                alert("${mail_footer_webhat_copied}");
            });
        }
    </script>
</div>
</body>
</html>