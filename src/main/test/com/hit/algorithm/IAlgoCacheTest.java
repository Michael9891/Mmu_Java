package com.hit.algorithm;

public class IAlgoCacheTest {
	
	public static void main(String[] args){
		LRUAlgoCacheImpl<Integer, String> lru = new LRUAlgoCacheImpl<>(3);
		MRUAlgoCacheImpl<Integer, String> mru = new MRUAlgoCacheImpl<>(3);
		RandomReplacementLAlgoCacheImpl<Integer, String> rr = new RandomReplacementLAlgoCacheImpl<>(3);
		System.out.println("***************************** LRU ");
		String s = lru.putElement(1, "page1");
		System.out.println("page fault need return null ---->"+s);
		s = lru.putElement(2, "page2");
		System.out.println("page fault need return null ---->"+s);
		s = lru.putElement(3, "page3");
		System.out.println("page fault need return null ---->"+s);
		s = lru.putElement(4, "page4");
		System.out.println("replacement 1 is last ,need return 1 ---->"+s);
		s = lru.putElement(5, "page5");
		System.out.println("replacement 2 is last ,need return 2 ---->"+s);
		s = lru.putElement(5, "page5");
		System.out.println("5 in the ram ,need return null ---->"+s);
		System.out.println("\n\n\n");
		
		System.out.println("***************************** MRU ");
		s = mru.putElement(1, "page1");
		System.out.println("page fault need return null ----> "+s);
		s = mru.putElement(2, "page2");
		System.out.println("page fault need return null ----> "+s);
		s = mru.putElement(3, "page3");
		System.out.println("page fault need return null ----> "+s);
		s = mru.putElement(4, "page4");
		System.out.println("replacement 3 is first need return 3 ----> "+s);
		s = mru.putElement(5, "page5");
		System.out.println("replacement 4 is firs need return 4 ----> "+s);
		s = mru.putElement(5, "page5");
		System.out.println("5 in the ram need return null ----> "+s);
		System.out.println("\n\n\n");
		
		System.out.println("***************************** RR ");
		s = rr.putElement(1, "page1");
		System.out.println("page fault need return null ----> "+s);
		s = rr.putElement(2, "page2");
		System.out.println("page fault need return null ----> "+s);
		s = rr.putElement(3, "page3");
		System.out.println("page fault need return null ----> "+s);
		s = rr.putElement(4, "page4");
		System.out.println("replacement , return some page but not null ----> "+s);
		s = rr.putElement(5, "page5");
		System.out.println("replacement , return some page but not null ----> "+s);
		s = rr.putElement(6, "page6");
		System.out.println("replacement , return some page but not null ----> "+s);
		s = rr.putElement(7, "page7");
		System.out.println("replacement , return some page but not null ----> "+s);
		s = rr.putElement(7, "page7");
		System.out.println("7 in the ram , return null ----> "+s);
		
	}

}
