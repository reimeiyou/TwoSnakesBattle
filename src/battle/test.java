package battle;

import java.util.HashSet;
import java.util.Set;

public class test {
	public static void main(String args[]){
		Set<String> set = new HashSet<String>();
		set.add(new Coordinate(10,10).toString());
		set.add(new Coordinate(11,10).toString());
		set.add(new Coordinate(10,11).toString());
		set.add(new Coordinate(9,10).toString());
		set.add(new Coordinate(10,10).toString());
		set.add(new Coordinate(11,10).toString());
		set.add(new Coordinate(10,11).toString());
		set.add(new Coordinate(9,10).toString());
		for (String c : set){
			System.out.println(c);
		}
	}
}