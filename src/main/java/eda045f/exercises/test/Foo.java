package eda045f.exercises.test;

import eda045f.exercises.test.Goo;

public class Foo {
	public static void main(String... args) {
		Foo foo = new Foo();
		int a = 7;
		int b = 14;
		int x = (foo.bar(21) + a) * b + Math.abs(-1);
		
		foo.barssa(1);
		System.out.println(foo.barssa(2));
	}
	
	public int bar(int n) {
		new Goo().goo();
		return n + 42 + this.five();
	}
	
	int five() {
		return 5;
	}
	@Deprecated
	public int barssa(int i) {
		int x,y,z;
		x = 0;
		y = 10;
		z = 22;
		System.out.println(z + i);
//		while(x < y) {
//			if(x % y == 0) {
//				z = x;
//			}else {
//				z = y;
//			}
//			System.out.println(z);
//		}
//		System.out.println(x);
		
		return z;
	}
	
	/**
	 * @deprecated
	 */
	public void badUsage() {
		
	}
	
	private static class Boo {
		public static void main(String args) {
			
		}
	}
}
