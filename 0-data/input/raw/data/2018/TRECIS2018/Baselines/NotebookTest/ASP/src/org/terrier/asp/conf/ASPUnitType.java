package org.terrier.asp.conf;

/**
 * ASP Units can be of two main types, either batch or stream.
 * Batch units process a Spark dataset as one big operation, while
 * Stream units belong as part of a larger stream computation
 * and are processed item by item.
 * @author richardm
 *
 */
public enum ASPUnitType {

	batch,
	stream
	
}
