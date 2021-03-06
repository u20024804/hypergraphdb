package hgtest.storage.bje.BJETxLock;

import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.storage.bje.BJETxLock;
import org.powermock.api.easymock.PowerMock;
import org.junit.Test;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.assertNotNull;

/**
 * @author Yuriy Sechko
 */
public class BJETxLock_readLockTest
{
	@Test
	public void test() throws Exception
	{
		final HyperGraph graph = PowerMock.createStrictMock(HyperGraph.class);
		PowerMock.replayAll();
		final byte[] objectId = new byte[] { 0, 0, 0, 1 };
		final BJETxLock bjeLock = new BJETxLock(graph, objectId);

		final Lock readLock = bjeLock.readLock();

		assertNotNull(readLock);
	}
}
