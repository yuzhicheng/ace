package com.yzc.utils.email;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.util.MailSSLSocketFactory;

public class EmailUtils {

	private static final Logger LOG = LoggerFactory.getLogger(EmailUtils.class);

	// 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般格式为: smtp.xxx.com
	// 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
	// QQ邮箱的 SMTP 服务器地址为: smtp.qq.com
	public static String EMAIL_SMTP_HOST = "smtp.qq.com";
	// 发件人的邮箱和密码()
	public static String EMAIL_ACCOUNT = "xxxxx@qq.com";
	public static String EMAIL_PASSWORD = "ljehpzswhjshehfh";

	/**
	 * 私有化构造函数，不允许实例化该类
	 */
	private EmailUtils() {

	}

	public static void main(String[] args) throws GeneralSecurityException, MessagingException {

		System.out.println(new Date());
		// String content="hello\n吃饭了么\n";
		String content = "<html><head><style type=\"text/css\">#preview, .img, img {width: 200px;height: 200px;}#preview {border: 1px solid #000;}</style></head><body><div id=\"preview\"></div><input type=\"file\" onchange=\"preview(this)\" />"
				+ "<script type=\"text/javascript\">function preview(file) {var prevDiv = document.getElementById('preview');if (file.files && file.files[0]) {var reader = new FileReader();reader.onload = function (evt) {prevDiv.innerHTML = '<img src=\"' + evt.target.result + '\" />';}reader.readAsDataURL(file.files[0]);}else {prevDiv.innerHTML = '<div class=\"img\" style=\"filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src=\''+ file.value + '\'\"></div>';}}</script>"
				+ "</body></html>";
		String to = "582771729@qq.com";

		EmailInfo emailInfo = new EmailInfo();
		emailInfo.setContent(content);
		emailInfo.setFromAddress(EMAIL_ACCOUNT);
		List<String> toList = new ArrayList<>();
		toList.add(to);
		// toList.add("beasion@sina.com");
		emailInfo.setToAddress(toList);
		emailInfo.setDate(new Date());
		emailInfo.setSubject("测试邮件");
		emailInfo.setUserName(EMAIL_ACCOUNT);
		emailInfo.setPassword(EMAIL_PASSWORD);
		sendTextEmail(emailInfo);
		sendHtmlEmail(emailInfo);
		sendAttachEmail(emailInfo);

	}

	/**
	 * 获取会话对象，用于和邮件服务器交互
	 */
	private static Session getSession() {
		// 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties();
		props.setProperty("mail.debug", "true");// 设置为debug模式, 可以查看详细的发送 log
		props.setProperty("mail.smtp.auth", "true");// 需要请求认证
		props.setProperty("mail.host", "smtp.qq.com");// 发件人的邮箱的 SMTP 服务器地址
		props.setProperty("mail.transport.protocol", "smtp");// 使用的协议（JavaMail规范要求）

		// 开启 SSL 连接
		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
		} catch (GeneralSecurityException e) {
			LOG.error("开启 SSL 连接错误：" + e);
			e.printStackTrace();
		}

		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.socketFactory", sf);
		Session session = Session.getInstance(props);

		return session;
	}

	public static void sendTextEmailByQQ(String to, String content) {

		Message msg = new MimeMessage(getSession());
		try {
			msg.setText(content);
			msg.setFrom(new InternetAddress("1799281706@qq.com"));
			msg.setSubject("send email test");
			msg.setSentDate(Calendar.getInstance().getTime());
			Transport transport = getSession().getTransport();
			transport.connect("smtp.qq.com", "1799281706@qq.com", "ljehpzswhjshehfh");
			transport.sendMessage(msg, new Address[] { new InternetAddress(to) });
			transport.close();
		} catch (MessagingException e) {

			e.printStackTrace();
		}

	}

	public static void sendHtmlEmailByQQ(String to, String content) {

		Message msg = new MimeMessage(getSession());
		try {
			// msg.setText(content);
			msg.setFrom(new InternetAddress("1799281706@qq.com"));
			msg.setSubject("send email test");
			msg.setSentDate(Calendar.getInstance().getTime());
			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(content, "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			msg.setContent(mainPart);
			Transport transport = getSession().getTransport();
			transport.connect("smtp.qq.com", "1799281706@qq.com", "ljehpzswhjshehfh");
			transport.sendMessage(msg, new Address[] { new InternetAddress(to) });
			transport.close();
		} catch (MessagingException e) {

			e.printStackTrace();
		}

	}

	public static void sendEmailwithAttachByQQ(String to, String content) throws UnsupportedEncodingException {

		// 1. 创建邮件对象
		Message msg = new MimeMessage(getSession());
		try {
			// msg.setText(content);
			msg.setFrom(new InternetAddress("1799281706@qq.com"));
			msg.setSubject("send email with attachments test");
			msg.setSentDate(Calendar.getInstance().getTime());
			MimeBodyPart image = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource("D:\\桌面\\1.jpg")); // 读取本地文件
			image.setDataHandler(dh); // 将图片数据添加到“节点”
			image.setContentID("image_fairy_tail"); // 为“节点”设置一个唯一编号（在文本“节点”将引用该ID）
			// 6. 创建文本“节点”
			MimeBodyPart text = new MimeBodyPart();
			// 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
			text.setContent("这是一张图片<br/><img src='cid:image_fairy_tail'/>", "text/html;charset=UTF-8");
			// 7. （文本+图片）设置 文本 和 图片 “节点”的关系（将 文本 和 图片 “节点”合成一个混合“节点”）
			MimeMultipart mm_text_image = new MimeMultipart();
			mm_text_image.addBodyPart(text);
			mm_text_image.addBodyPart(image);
			mm_text_image.setSubType("related"); // 关联关系
			// 8. 将 文本+图片 的混合“节点”封装成一个普通“节点”
			// 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
			// 上面的 mm_text_image 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
			MimeBodyPart text_image = new MimeBodyPart();
			text_image.setContent(mm_text_image);

			// 9. 创建附件“节点”
			MimeBodyPart attachment = new MimeBodyPart();
			DataHandler dh2 = new DataHandler(new FileDataSource("D:\\桌面\\FileController.java")); // 读取本地文件
			attachment.setDataHandler(dh2); // 将附件数据添加到“节点”
			attachment.setFileName(MimeUtility.encodeText(dh2.getName())); // 设置附件的文件名（需要编码）

			// 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合“节点” / Multipart ）
			MimeMultipart mm = new MimeMultipart();
			mm.addBodyPart(text_image);
			mm.addBodyPart(attachment); // 如果有多个附件，可以创建多个多次添加
			mm.setSubType("mixed"); // 混合关系

			// 11. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
			msg.setContent(mm);
			
	        // 13. 保存上面的所有设置
			msg.saveChanges();
	        
			Transport transport = getSession().getTransport();
			transport.connect("smtp.qq.com", "1799281706@qq.com", "ljehpzswhjshehfh");
			transport.sendMessage(msg, new Address[] { new InternetAddress(to) });
			transport.close();
		} catch (MessagingException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 以文本格式发送邮件
	 * 
	 * @author yzc
	 * @date 2017年4月18日
	 * @param emailInfo
	 *            待发送的邮件信息
	 */
	public static void sendTextEmail(EmailInfo emailInfo) {

		Message message = new MimeMessage(getSession());
		try {
			message.setText(emailInfo.getContent());
			message.setFrom(new InternetAddress(emailInfo.getFromAddress()));
			message.setSubject(emailInfo.getSubject());
			message.setSentDate(emailInfo.getDate());
			for (String to : emailInfo.getToAddress()) {
				message.addRecipient(RecipientType.TO, new InternetAddress(to));
			}		
	        message.saveChanges();

			Transport transport = getSession().getTransport();
			transport.connect(emailInfo.getUserName(), emailInfo.getPassword());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (MessagingException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 以HTML格式发送邮件
	 * 
	 * @author yzc
	 * @date 2017年4月18日
	 * @param 待发送的邮件信息
	 */
	public static void sendHtmlEmail(EmailInfo emailInfo) {

		Message message = new MimeMessage(getSession());
		try {
			message.setFrom(new InternetAddress(emailInfo.getFromAddress()));
			message.setSubject(emailInfo.getSubject());
			message.setSentDate(emailInfo.getDate());
			for (String to : emailInfo.getToAddress()) {
				message.addRecipient(RecipientType.TO, new InternetAddress(to));
			}

			// MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 创建一个包含HTML内容的MimeBodyPart
			BodyPart html = new MimeBodyPart();
			// 设置HTML内容
			html.setContent(emailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			// 将MiniMultipart对象设置为邮件内容
			message.setContent(mainPart);
		
	        message.saveChanges();

			Transport transport = getSession().getTransport();
			transport.connect(emailInfo.getUserName(), emailInfo.getPassword());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (MessagingException e) {

			e.printStackTrace();
		}

	}

	/**
	 * 以（文本+图片+附件）格式发送邮件
	 * 
	 * @author yzc
	 * @date 2017年4月18日
	 * @param emailInfo
	 */
	public static void sendAttachEmail(EmailInfo emailInfo) {

		Message message = new MimeMessage(getSession());
		try {
			message.setFrom(new InternetAddress(emailInfo.getFromAddress()));
			message.setSubject(emailInfo.getSubject());
			message.setSentDate(emailInfo.getDate());
			for (String to : emailInfo.getToAddress()) {
				message.addRecipient(RecipientType.TO, new InternetAddress(to));
			}

			// 5. 创建图片"节点"
			MimeBodyPart image = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource("D:\\桌面\\1.jpg")); // 读取本地文件
			image.setDataHandler(dh); // 将图片数据添加到“节点”
			image.setContentID("image_unique_id"); // 为“节点”设置一个唯一编号（在文本“节点”将引用该ID）
			// 6. 创建文本“节点”
			MimeBodyPart text = new MimeBodyPart();
			// 这里添加图片的方式是将整个图片包含到邮件内容中, 实际上也可以以 http 链接的形式添加网络图片
			text.setContent(emailInfo.getContent() + "<br/><img src='cid:image_unique_id'/>","text/html;charset=UTF-8");
			// 7. （文本+图片）设置 文本 和 图片 “节点”的关系（将 文本 和 图片 “节点”合成一个混合“节点”）
			MimeMultipart mm_text_image = new MimeMultipart();
			mm_text_image.addBodyPart(text);
			mm_text_image.addBodyPart(image);
			mm_text_image.setSubType("related"); // 关联关系
			// 8. 将 文本+图片 的混合“节点”封装成一个普通“节点”
			// 最终添加到邮件的 Content 是由多个 BodyPart 组成的 Multipart, 所以我们需要的是 BodyPart,
			// 上面的 mm_text_image 并非 BodyPart, 所有要把 mm_text_image 封装成一个 BodyPart
			MimeBodyPart text_image = new MimeBodyPart();
			text_image.setContent(mm_text_image);

			// 9. 创建附件“节点”
			MimeBodyPart attachment = new MimeBodyPart();
			DataHandler dh2 = new DataHandler(new FileDataSource("D:\\桌面\\FileController.java")); // 读取本地文件
			attachment.setDataHandler(dh2); // 将附件数据添加到“节点”
			attachment.setFileName(MimeUtility.encodeText(dh2.getName())); // 设置附件的文件名（需要编码）

			// 10. 设置（文本+图片）和 附件 的关系（合成一个大的混合“节点” / Multipart ）
			MimeMultipart mm = new MimeMultipart();
			mm.addBodyPart(text_image);
			mm.addBodyPart(attachment); // 如果有多个附件，可以创建多个多次添加
			mm.setSubType("mixed"); // 混合关系

			// 11. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
			message.setContent(mm);
			
	        // 12. 保存上面的所有设置
	        message.saveChanges();
	        // 13. 根据 Session 获取邮件传输对象
			Transport transport = getSession().getTransport();
			// 14. 使用 邮箱账号 和 密码 连接邮件服务器
			transport.connect(EMAIL_SMTP_HOST, emailInfo.getUserName(), emailInfo.getPassword());
			// 15. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (MessagingException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}

	}

}
