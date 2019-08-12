package de.m3y3r.nsmtp.model.imf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import de.m3y3r.nsmtp.mailbox.Mailbox;
import de.m3y3r.nsmtp.util.Path;

public class Headers {

	private final List<Header> headers = new ArrayList<>();

	// 3.6.1.  The Origination Date Field
	private MailDate origDate;

	// 3.6.2.  Originator Fields
	private List<Mailbox> from;
	private Mailbox sender; // mandatory if from > 1
	private List<Address> replyTo;

	// 3.6.3.  Destination Address Fields
	private List<Address> to;
	private List<Address> cc;
	private List<Address> bcc;

	// 3.6.4.  Identification Fields
	private MessageId messageId;
	private List<MessageId> inReplyTo; // one or more
	private List<MessageId> references; // one or more

	// 3.6.5.  Informational Fields
	private String subject;
	private String comments;
	private List<Keyword> keywords;

	// 3.6.6.  Resent Fields
	private MailDate resentDate;
	private List<Mailbox> resentFrom;
	private Mailbox resentSender; // mandatory if from > 1
	private List<Address> resentTo;
	private List<Address> resentCc;
	private List<Address> resentBcc;
	private MessageId resentMessageId;

	// 3.6.7.  Trace Fields
	private Path returnPath; // optional
	private List<ReceivedTrace> received;

	public Headers() {}

	public List<Header> getHeaders() {
		return Collections.unmodifiableList(headers);
	}

	public void addHeader(Header header) {
		this.headers.add(header);
	}

//	private static interface ConstraintValidator {
//	boolean accept(Headers hs, Header h);
//}
//
//private static class MaxOccConstraint implements ConstraintValidator {
//	private final String name;
//	private final long maxOcc;
//
//	public MaxOccConstraint(String name, long maxOcc) {
//		this.name = name;
//		this.maxOcc = maxOcc;
//	}
//
//	@Override
//	public boolean accept(Headers hs, Header h) {
//		hs.getHeaders().stream().filter(p -> p.getName().equals(h.getName())).count();
//	}
//}

/** constaints for headers */
private static final Map<Header, List<Object>> headerConstaints = new HashMap<>();
//{
//	BiFunction<Headers, Header, Long> occurens = (hs, h) -> 
//	BiFunction<String, Long> maxOcc = (name, max)
//}

	/**
	 * 
	"The following table indicates limits on the number of times each
     field may occur in the header section of a message as well as any
     special limitations on the use of those fields."
	 * @param model 
	 * @param header
	 */
	private void validateModel(Header header) {
		// get all constraints for the current header 
		List<Object> constraints = headerConstaints.get(header);
		for(Object constraint: constraints) {
//			Throwable t = constraint.test(getHeaders(), header);
//			if(t !=  null) throw t;
		}
	}

	public void validate() {
	}
}
