package io.github.mojtab23.diaries;

import io.github.mojtab23.diaries.binding.InstantBinding;
import jetbrains.exodus.ArrayByteIterable;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;

/**
 * Created by mojtab23 on 5/16/2017.
 */
public class InstantBindingTest {


    @Test
    public void instantToEntry() throws Exception {

        final Instant now = Instant.now();
        System.out.println("now: " + now);

        final ArrayByteIterable entry = InstantBinding.instantToEntry(now);
        final Instant instant = InstantBinding.entryToInstant(entry);
        System.out.println("and: " + instant);

        Assert.assertEquals(instant, now);


    }

}