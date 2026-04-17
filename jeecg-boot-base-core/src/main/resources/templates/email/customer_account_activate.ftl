<!DOCTYPE html>
<html>
<body>
<table width="600px">
    <tr>
        <td>
            <h2>${mail_title}</h2>

            <p>${mail_content}</p>

            <a href="${activationLink}"
               style="background:#1a73e8;color:white;padding:10px 20px;text-decoration:none;">
                ${mail_button}
            </a>
        </td>
    </tr>
</table>

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
