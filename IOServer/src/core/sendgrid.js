import sgMail from "@sendgrid/mail";
import env from "@core/env";

sgMail.setApiKey(env.sendgridKey);

const defaultSender = "librarian@fptu.tech",
    defaultSubject = "Thông báo từ phòng Thư viện Trường Đại học FPT",
    defaultText = "Phòng thư viện xin gửi thông báo đến bạn",
    defaultHtml = "<strong>Phòng thư viện xin gửi thông báo đến bạn</strong>";

export async function sendEmail(datas) {
    const to = datas.to || "",
        from = datas.from || defaultSender,
        subject = datas.subject || defaultSubject,
        text = datas.text || defaultText,
        html = datas.html || defaultHtml;

    try {
        return await sgMail.send({ to, from, subject, text, html });
    } catch (err) {
        throw err;
    }
}
