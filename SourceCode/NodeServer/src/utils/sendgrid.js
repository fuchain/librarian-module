import sgMail from "@sendgrid/mail";

sgMail.setApiKey(process.env.SENDGRID_API_KEY);

const
    defaultSender = "librarian@fe.edu.vn",
    defaultSubject = "Thông báo từ phòng thư viện",
    defaultText = "Phòng thư viện xin gửi thông báo đến bạn",
    defaultHtml = "<strong>Phòng thư viện xin gửi thông báo đến bạn</strong>";

export function sendEmail(datas) {
    const
        to = datas["to"] || "",
        from = datas["from"] || defaultSender,
        subject = datas['subject'] || defaultSubject,
        text = datas['text'] || defaultText,
        html = datas['html'] || defaultHtml;

    sgMail.send({ to, from, subject, text, html });
}
