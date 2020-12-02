package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PerRecordNGramsTest {

	private PerRecordNGrams func = new PerRecordNGrams();

    private ArrayList<String> empty = new ArrayList<String>();

    @Test
    public void emptyListReturnedForNullText() throws HiveException {
        assertEquals(empty, func.evaluate(3, null));
    }

    @Test
    public void emptyListReturnedForEmptyText() throws HiveException {
        assertEquals(empty, func.evaluate(3, ""));
    }

    @Test(expected = HiveException.class)
    public void throwsIfNIsNegative() throws HiveException {
        func.evaluate(-1, null);
    }

    @Test(expected = HiveException.class)
    public void throwsIfNEqualsZero() throws HiveException {
        func.evaluate(0, null);
    }

    @Test
    public void emptyListReturnedForTextWithFewerThanNWords() throws HiveException {
        assertEquals(empty, func.evaluate(3, "Now is"));
    }

	@Test
	public void oneElementListReturnedForTextWithNWords() throws HiveException {
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("Now is the");
		assertEquals(expected, func.evaluate(3, "Now is the"));
	}

	@Test
	public void manyElementListReturnedForTextWithMoreThanNWords() throws HiveException {
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("Now is the");
		expected.add("is the time");
		assertEquals(expected, func.evaluate(3, "Now is the time"));

		expected = new ArrayList<String>();
		expected.add("Now is the");
		expected.add("is the time");
		expected.add("the time for");
		expected.add("time for all");
		expected.add("for all good");
		expected.add("all good men");
		assertEquals(expected, func.evaluate(3, "Now is the time for all good men"));
	}

	@Test
	public void leadingAndTrailingWhitespaceIsIgnored() throws HiveException {
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("Now is the");
		expected.add("is the time");
		assertEquals(expected, func.evaluate(3, " \tNow is the time \t"));
	}

	@Test
	public void punctuationIsTreatedAsWhitespace() throws HiveException {
		ArrayList<String> expected = new ArrayList<String>();
		expected.add("Now is the");
		expected.add("is the time");
		assertEquals(expected, func.evaluate(3, "?Now-is.the ! time ;"));
	}
}
