package eda045f.exercises.test;

public class Foo {
	public static void main(String[] args) {
		Foo foo = new Foo();
		int a = 7;
		int b = 14;
		int x = (foo.bar(21) + a) * b;
	}
	
	public int bar(int n) {
		return n + 42;
	}
}
