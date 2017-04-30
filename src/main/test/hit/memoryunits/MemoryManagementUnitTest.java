package hit.memoryunits;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;

import MemoruUnits.MemoryManagementUnit;
import MemoruUnits.Page;
public class MemoryManagementUnitTest {
	private IAlgoCache<Long,Long>algo;
	private MemoryManagementUnit mmu;
	
	public MemoryManagementUnitTest() {
		algo = new LRUAlgoCacheImpl<Long, Long>(3);
		mmu = new MemoryManagementUnit(3, algo);
	}
	
	@Test
	public void getPages() throws Exception {
		Page<byte[]>[] p;
		Long[] pageIds = { (long) 1, (long) 2,(long) 34,(long) 4,(long) 86,(long) 10};
		boolean[] wr = new boolean[6];
		Arrays.fill(wr, true);
		p = mmu.getPages(pageIds,wr);
		for (int i = 0; i < p.length; i++) {
			System.out.println(p[i].getPageId());
			assertEquals(pageIds[i], p[i].getPageId());    //check if get pages return all pages that requared
		}

	}

}
