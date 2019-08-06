package de.m3y3r.nsmtp.model.imf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Headers {

	private final List<Header> headers = new ArrayList<>();

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
}
