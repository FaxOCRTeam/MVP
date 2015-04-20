package watchdog;

import gui.models.FormField;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;

import net.sourceforge.tess4j.TesseractException;
import processor.ContentProcessor;
import processor.ImageProcessor;
import utils.ConfigurationUtil;
import dataModel.ConfigField;
import dataModel.Field;

public class EmailWatchDog {
	final String watchDogConfigKey = "watchdog";
	private String email;
	private String pass;
	private File storageFolder;
	private String host;
	private String modelDirPath;

	public EmailWatchDog() {
		email = ConfigurationUtil.get(watchDogConfigKey, "email");
		pass = ConfigurationUtil.get(watchDogConfigKey, "password");
		String storagePath = ConfigurationUtil.get(watchDogConfigKey, "storagePath");
		host = ConfigurationUtil.get(watchDogConfigKey, "host");
		modelDirPath = ConfigurationUtil.get(watchDogConfigKey, "model.dir");

		storageFolder = new File(storagePath);
		if (storageFolder.exists() && !storageFolder.isDirectory())
			storageFolder.delete();
		if (!storageFolder.exists())
			storageFolder.mkdirs();

		init();
	}

	Folder inboxFolder = null;
	Store store = null;

	public void init() {
		Properties props = new Properties();
		// props.put("mail.store.protocol", "pop3s"); // Google uses POP3S not
		// POP3
		props.put("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props);
		try {
			store = session.getStore("imaps");
			store.connect(host, email, pass);
			inboxFolder = store.getDefaultFolder().getFolder("INBOX");
			inboxFolder.open(Folder.READ_WRITE);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		int count = 0;
		while (true) {
			if (count++ % 12 == 0)
				System.out.println("No message read");
			checkUnread();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void checkUnread() {
		try {
			// System.out.println(inboxFolder.getNewMessageCount());
			System.out.println("count: " + inboxFolder.getUnreadMessageCount());
			if (inboxFolder.getUnreadMessageCount() == 0) {
				return;
			}
			Message[] messages = inboxFolder.getMessages();
			for (Message msg : messages) {
				boolean read = msg.isSet(Flag.SEEN);
				if (read)
					continue;
				String from = "unknown";
				if (msg.getReplyTo().length >= 1) {
					from = msg.getReplyTo()[0].toString();
				} else if (msg.getFrom().length >= 1) {
					from = msg.getFrom()[0].toString();
				}
				String subject = msg.getSubject();
				System.out.println(read ? "read" : "unread");
				System.out.println("Saving ... " + subject + " " + from);
				// you may want to replace the spaces with "_"
				// the files will be saved into the TEMP directory
				File savePic = null;
				String contentType = msg.getContentType();
				if (contentType.contains("multipart")) {
					Multipart multiPart = (Multipart) msg.getContent();

					for (int i = 0; i < multiPart.getCount(); i++) {
						MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
						if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
							File file = new File(part.getFileName());
							part.saveFile(file);
							if (file.getName().endsWith("jpg"))
								savePic = file;
						}
					}
				}

				// try {
				// savePic = saveParts(msg.getContent(), subject);
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				if (savePic != null) {
					System.out.println("Processing : " + savePic.getAbsolutePath());
					List<FormField> model = FormField
							.loadModel(new File(modelDirPath, subject));
					List<ConfigField> transformModel = new ArrayList<ConfigField>();
					for (FormField ff : model) {
						transformModel.add(ff.toConfigField());
					}
					ImageProcessor processor = new ImageProcessor();
					List<Field> process = processor.process(transformModel,
							savePic.getAbsolutePath());
					ContentProcessor contentProcessor = new ContentProcessor();
					try {
						contentProcessor.process(process);
					} catch (TesseractException e) {
						e.printStackTrace();
					}
					savePic.delete();
				}
				// break;
			}
			System.out.println(messages.length);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File saveParts(Object content, String subject) throws IOException,
			MessagingException {
		OutputStream out = null;
		InputStream in = null;
		File result = null;
		try {
			if (content instanceof Multipart) {
				Multipart multi = ((Multipart) content);
				int parts = multi.getCount();
				System.out.println("parts : " + parts);
				for (int j = 0; j < parts; ++j) {
					MimeBodyPart part = (MimeBodyPart) multi.getBodyPart(j);
					if (part.getContent() instanceof Multipart) {
						// part-within-a-part, do some recursion...
						return saveParts(part.getContent(), subject);
					} else {
						String extension = "";
						if (part.isMimeType("text/html")) {
							extension = "html";
						} else {
							if (part.isMimeType("text/plain")) {
								extension = "txt";
							} else {
								// Try to get the name of the attachment
								extension = part.getDataHandler().getName();
							}
						}

						// String filename = subject + "." + extension;
						String filename = subject + "." + extension;
						File file = new File(storageFolder, filename);
						System.out.println("display file: " + file.getAbsolutePath());
						if (file.getName().toLowerCase().endsWith(".jpg"))
							result = file;
						out = new FileOutputStream(file);
						in = part.getInputStream();
						int k;
						while ((k = in.read()) != -1) {
							out.write(k);
						}
					}
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		EmailWatchDog wd = new EmailWatchDog();
		wd.start();
	}
}
