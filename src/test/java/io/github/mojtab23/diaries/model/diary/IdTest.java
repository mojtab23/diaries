package io.github.mojtab23.diaries.model.diary;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by mojtab23 on 6/4/2017.
 */
public class IdTest {


    @Test
    public void testId() throws Exception {

        final Id id = new Id();
        System.out.println(id);
        final Id id1 = Id.createId(id.toByteArray());
        Assert.assertEquals(id, id1);
        Thread.sleep(1100);
        final Id id2 = new Id();
        System.out.println(id2);
        if (id.compareTo(id2) >= 0) {
            Assert.fail();
        }

    }
}