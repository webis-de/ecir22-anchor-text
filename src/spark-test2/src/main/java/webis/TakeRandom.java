package webis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

public class TakeRandom {

	public static <T> List<T> takeRandomElements(int k, Iterable<T> input) {
		return takeRandomElements(k, input, new Random());
	}
	
	public static <T> List<T> takeRandomElements(int k, Iterable<T> input, Random rand) {
		Iterator<Pair<Double, T>> iter = Iterators.transform(input.iterator(), i -> Pair.of(rand.nextDouble(), i));
		return takeRandomElements(k, iter);
	}
	
	private static <T> List<T> takeRandomElements(int k, Iterator<Pair<Double, T>> iter) {
		PriorityQueue<Pair<Double, T>> ret = new PriorityQueue<>(2*k, (a,b) -> a.getKey().compareTo(b.getKey()));
		iter.forEachRemaining(i -> {
			ret.add(i);
			
			if(ret.size() > k) {
				ret.poll();
			}
		});
		
		return ImmutableList.copyOf(ret.iterator()).stream()
				.map(i -> i.getRight())
				.collect(Collectors.toList());
	}

	public static <T> List<T> takeFirst(int k, Iterable<T> iterable) {
		List<T> ret = new ArrayList<>();
		Iterator<T> iter = iterable.iterator();
		
		while(iter.hasNext() && ret.size() < k) {
			ret.add(iter.next());
		}
		
		return ret;
	}
}
