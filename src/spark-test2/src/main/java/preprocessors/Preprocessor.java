package preprocessors;

import java.io.Serializable;

import anchor.AnchorElement;

public interface Preprocessor extends Serializable {
	// returns the processed version of an AnchorElement
	public AnchorElement processElement(AnchorElement element);
}