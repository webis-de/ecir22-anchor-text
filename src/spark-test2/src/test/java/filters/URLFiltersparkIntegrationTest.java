package filters;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;
import org.junit.Test;

import com.holdenkarau.spark.testing.SharedJavaSparkContext;

import anchor.AnchorElement;

public class URLFiltersparkIntegrationTest extends SharedJavaSparkContext {
	
	@Test
	public void test() {
		JavaRDD<String> rdd = jsc().parallelize(Arrays.asList("a","b","c"));
		
		OnlyInternalURIsFilter filter = new OnlyInternalURIsFilter(null, "id0 b");
		
		List<String> expected = Arrays.asList("b");
	
		
		List<String> actual = rdd.map(s -> new AnchorElement(null, s, null))
				.filter(s -> filter.filterElement(s))
				.map(s -> s.getTargetUrl()).collect();
		
		assertEquals(expected, actual);
		
	}
}
