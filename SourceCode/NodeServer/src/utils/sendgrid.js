import sgMail from "@sendgrid/mail";
import env from "@utils/env";

sgMail.setApiKey(env.sendgridKey);

const defaultSender = "librarian@fe.edu.vn",
    defaultSubject = "Thông báo từ phòng thư viện",
    defaultText = "Phòng thư viện xin gửi thông báo đến bạn",
    defaultHtml = "<strong>Phòng thư viện xin gửi thông báo đến bạn</strong>";

export function sendEmail(datas) {
    const to = datas.to || "",
        from = datas.from || defaultSender,
        subject = datas.subject || defaultSubject,
        text = datas.text || defaultText,
        html = datas.html || defaultHtml;

    sgMail.send({ to, from, subject, text, html });
}
