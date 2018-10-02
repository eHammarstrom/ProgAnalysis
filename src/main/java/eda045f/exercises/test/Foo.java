package eda045f.exercises.test;

public class Foo {
//	public static void main(String... args) {
//		Foo foo = new Foo();
//		int a = 7;
//		int b = 14;
//		int x = (foo.bar(21) + a) * b + Math.abs(-1);
//		
//		foo.barssa(1);
//		System.out.println(foo.barssa(2));
//	}
//	
//	public int bar(int n) {
//		new Goo().goo();
//		return n + 42 + this.five();
//	}
//	
//	int five() {
//		return 5;
//	}
//	@Deprecated
//	public int barssa(int i) {
//		int x,y,z;
//		x = 0;
//		y = 10;
//		z = 22;
//		System.out.println(z + i);
////		while(x < y) {
////			if(x % y == 0) {
////				z = x;
////			}else {
////				z = y;
////			}
////			System.out.println(z);
////		}
////		System.out.println(x);
//		
//		return z;
//	}
//
//	/**
//	 * @deprecated
//	 */
//	public void badUsage() {
//		int[] a = new int[5];
//		a[-1] = 5;
//		a[0] = 1;
//		a[1] = 2;
//		int i = 3;
//		int j = 4;
//		int z = -5;
//		int m = -1;
//		int p = 100;
//		int l = -100;
//		while (true) {
//			i = i - 1;
//			j = -j + 1;
//			z = z - 5;
//			m = m * p;
//			p = p * 100;
//			l = z * 0;
//			System.out.println(a[i]);
//			System.out.println(a[j]);
//			System.out.println(a[z]);
//			System.out.println(a[m]);
//			System.out.println(a[p]);
//			System.out.println(a[l]);
//			i = a[-3];
//		}
//	}
//
//	public static int[] delta(int t) {
//		int[] d = new int[t];
//		for (int i = 0; i < t; ++i) {
//			d[i] = (i < 2) ? 1 : d[i - 1] + d[i - 2];
//			int unused = d[0];
//		}
//		return d;
//	}
//
//	static int sumup(int[] arg) {
//		int offset = 1;
//		int acc = arg[offset--];
//		acc += arg[offset--];
//		acc += arg[offset--];
//		return acc;
//	}
//
//	public void simple() {
//		int[] a = new int[5];
////		a[1] = 44;
////		a[2] = 55;
////		a[-1] = 333;
//
////		int j = -2;
////		int z = j;
//		int p = 3;
//
//		p = p - 4;
//		System.out.println(a[p]);
//
//		for (int i = 0; i <= 100; i++) {
////			j = j + 1;
////			z = j + 1;
//			p = p - 1;
////			a[i] = 3 + a[j] + a[z];
//			a[i] = a[p];
//		}
//	}
//
//	public void mul() {
//		int[] a = new int[5];
//		int x = 3;
//		int y;
//		while (true) {
//			x = -x;
//			y = -5 * x;
//			System.out.println(a[x]);
//			System.out.println(a[y]);
//		}
//	}
//
//	public int[] invertKey(int[] inKey) {
//		int t1, t2, t3, t4;
//		int p = 52; /* We work backwards */
//		int[] key = new int[52];
//		int inOff = 0;
//
//		t1 = 10;
//		t2 = 5;
//		t3 = 20;
//		t4 = 9;
//
//		key[--p] = t4;
//		key[--p] = t3;
//		key[--p] = t2;
//		key[--p] = t1;
//
//		for (int round = 1; round < 8; round++) {
//			t1 = inKey[inOff++];
//			t2 = inKey[inOff++];
//			key[--p] = t2;
//			key[--p] = t1;
//
//			key[--p] = t4;
//			key[--p] = t2; /* NB: Order */
//			key[--p] = t3;
//			key[--p] = t1;
//		}
//
//		t1 = inKey[inOff++];
//		t2 = inKey[inOff++];
//		key[--p] = t2;
//		key[--p] = t1;
//		key[--p] = t4;
//		key[--p] = t3;
//		key[--p] = t2;
//		key[--p] = t1;
//
//		return key;
//	}
//
//	static int getSome() {
//		return 5;
//	}
//
//	private static void implSumOfMultiplies(boolean[] negs, int[] infos, byte[][] wnafs) {
//		int len = 0, count = wnafs.length;
//		for (int i = 0; i < count; ++i) {
//			len = Math.max(len, wnafs[i].length);
//		}
//
//		int zeroes = 0;
//
//		for (int i = len - 1; i >= 0; --i) {
//
//			for (int j = 0; j < count; ++j) {
//				byte[] wnaf = wnafs[j];
//				int wi = i < wnaf.length ? wnaf[i] : 0;
//				if (wi != 0) {
//					int n = Math.abs(wi);
//					int info = infos[j];
//					int table = (wi < 0 == negs[j]) ? getSome() : getSome();
//				}
//			}
//		}
//	}
//	
//	void arrayRef() {
//		int[] a = new int[5];
//		int[] b = new int[5];
//		
//		b[0] = -1;
//		int x = b[0];
//		System.out.println(a[b[0]]);
//		System.out.println(a[x]);
//	}
	
	void livetest() {
		int z = 5;
		int x = 1;
		int y = 2;
		
		if(z > branchcond1()) { // not counted as use
			y = z;
			if(z < branchcond1()) {
				z = 7;
			}
		}
		System.out.println(y);
	}
	
	int branchcond1() { return 5; }

//	private static class Boo {
//		public static void main(String args) {
//			
//		}
//	}
//
//    void emil() {
//        int[] arr = new int[100];
//        int a = 3;
//        int aa = 0;
//        while (a > 0) {
//          aa = aa - 1;
//          a--;
//        }
//        arr[aa] = 19; // ZERO -> NEG // throws no warning
//    }
}
