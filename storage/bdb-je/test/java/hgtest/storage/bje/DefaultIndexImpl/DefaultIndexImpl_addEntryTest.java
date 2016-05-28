package hgtest.storage.bje.DefaultIndexImpl;

import static org.easymock.EasyMock.replay;

import org.hypergraphdb.HGException;
import org.hypergraphdb.storage.bje.DefaultIndexImpl;
import org.junit.Test;

public class DefaultIndexImpl_addEntryTest extends DefaultIndexImplTestBasis
{
	@Test
	public void throwsException_whenIndexIsNotOpenedAhead() throws Exception
	{
		replay(mockedStorage);

		index = new DefaultIndexImpl<>(INDEX_NAME, mockedStorage,
				transactionManager, keyConverter, valueConverter, comparator,
				null);

		below.expect(HGException.class);
		below.expectMessage("Attempting to operate on index 'sample_index' while the index is being closed.");
		index.addEntry(1, "one");
	}

	@Test
	public void throwsException_whenKeyIsNull() throws Exception
	{
		startupIndex();

		try
		{
			below.expect(NullPointerException.class);
			index.addEntry(null, "key is null");
		}
		finally
		{
			closeDatabase(index);
		}
	}

	@Test
	public void throwsException_whenValueIsNull() throws Exception
	{
		startupIndex();

		try
		{
			below.expect(NullPointerException.class);
			index.addEntry(2, null);
		}
		finally
		{
			closeDatabase(index);
		}
	}

	@Test
	public void wrapsUnderlyingException_withHypergraphException()
			throws Exception
	{
		startupIndexWithFakeTransactionManager();

		try
		{
			below.expect(HGException.class);
			below.expectMessage("Failed to add entry to index 'sample_index': java.lang.IllegalStateException: This exception is thrown by fake transaction manager.");
			index.addEntry(2, "two");
		}
		finally
		{
			index.close();
		}
	}
}
