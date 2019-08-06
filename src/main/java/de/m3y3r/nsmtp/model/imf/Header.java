package de.m3y3r.nsmtp.model.imf;

/**
 * https://tools.ietf.org/html/rfc5322
 *
 * 3.6.  Field Definitions
 * @author thomas
 *
 */
public class Header {

	private String name;
	private String body;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
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
		return null;
	}

}
