import sgMail from "@sendgrid/mail";

sgMail.setApiKey(process.env.SENDGRID_API_KEY);

const
    defaultSender = "librarian@fe.edu.vn",
    defaultSubject = "Notification from librarian",
    defaultText = "We have an announcement for you",
    defaultHtml = "<strong>We have an announcement for you</strong>";

export function sendEmail(
    to = "", from = defaultSender,
    subject = defaultSubject,
    text = defaultText, html = defaultHtml
) {
    const msg = { to, from, subject, text, html };
    sgMail.send(msg);
}