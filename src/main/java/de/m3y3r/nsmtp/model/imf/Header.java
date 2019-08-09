package de.m3y3r.nsmtp.model.imf;

/**
 * https://tools.ietf.org/html/rfc5322
 *
 * 3.6.  Field Definitions
 * @author thomas
 *
 */
public class Header {

	public static enum State {FIELD_NAME,  FIELD_BODY};

	private final String name;
	private final String body;

	public Header(String name, String body) {
		if(name == null || body == null) {
			throw new IllegalArgumentException();
		}

		//TODO: is it okay that name and body are empty?!
		if(name.isEmpty() && body.isEmpty()) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.body = body;
	}

	public String getName() {
		return name;
	}
	public String getBody() {
		return body;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Header other = (Header) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* the header lines must already be unfolded !! */
	public static Header parse(CharSequence line) {
		StringBuilder fieldName = new StringBuilder();
		StringBuilder fieldBody = new StringBuilder();

		State state = State.FIELD_NAME;
		for(int i = 0, n = line.length(); i < n; i++) {
			char c = line.charAt(i);
			if(state == State.FIELD_NAME) {
				if(c >= 33 && c <= 126) {
					// field name character is okay
					if(c == ':') { // 58!
						if(i == 0) {
							// first char is invalid, ftext must be at least one char, see 3.6.8.  Optional Fields
							throw new MissingFieldNameException();
						}
						state = State.FIELD_BODY;
					} else {
						fieldName.append(c);
					}
				} else {
					throw new IllegalCharacter(state, c);
				}
			} else if(state == State.FIELD_BODY) {
				if(c >= 32 && c <= 126 || c == 9) {
					// field body character is okay
					fieldBody.append(c);
				} else {
					throw new IllegalCharacter(state, c);
				}
			}
		}

		if(state == State.FIELD_NAME) {
			// missing colon!
			throw new MissingColonException(line);
		}
		Header header = new Header(fieldName.toString(), fieldBody.toString());

		validateHeader(header);
		return header;
	}

	private static void validateHeader(Header header) {
		
		switch(header.getName()) {
		case "Date": // orig-date
			validateDateTime(header.getBody());
			break;
		case "To":
			validateAddress(header.getBody());
		}
	}

	// 3.4.  Address Specification
	private static void validateAddress(String body2) {
	}

	// 3.3.  Date and Time Specification
	private static void validateDateTime(String body2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * CRLF is already removed, so when the first character is a WSP
	 * it is actually immediately followed by WSP
	 * @param line
	 * @return
	 */
	public static boolean isFolded(CharSequence line) {
		if(line == null) throw new IllegalArgumentException();

		//UGLY: special case: the current line is the break from headers to body, process as unfolded line!
		if(line.length() == 0) {
			return false;
		}

		char c = line.charAt(0);
		if(c == 32 || c == 9) { // is WSP
			return true;
		}
		return false;
	}

}
