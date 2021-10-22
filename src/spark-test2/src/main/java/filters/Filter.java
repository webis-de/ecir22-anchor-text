package filters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import anchor.AnchorElement;

public interface Filter extends Serializable {
	// returns false if the Anchor element should be filtered out, otherwise returns true 
	public boolean filterElement(AnchorElement element);
	
	@SuppressWarnings("serial")
	public static class Filters implements Filter {

		public final ArrayList<Filter> filters;
		
		public Filters(Filter... filters) {
			this.filters = new ArrayList<>(Arrays.asList(filters));
		}
		
		@Override
		public boolean filterElement(AnchorElement element) {
			Boolean ret = true;
			
			for(Filter filter : filters) {
				ret = ret && filter.filterElement(element);
			}
			return ret;
		}	
	}	
}
