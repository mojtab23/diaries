package io.github.mojtab23.diaries.binding;

import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.bindings.BindingUtils;
import jetbrains.exodus.bindings.ComparableBinding;
import jetbrains.exodus.util.LightOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.time.Instant;

/**
 * Created by mojtab23 on 5/16/2017.
 */
public class InstantBinding extends ComparableBinding {

    public static final InstantBinding BINDING = new InstantBinding();

    public static Instant entryToInstant(@NotNull final ByteIterable entry) {
        return (Instant) BINDING.entryToObject(entry);
    }

    public static ArrayByteIterable instantToEntry(final Instant object) {
        return BINDING.objectToEntry(object);
    }

    @Override
    public Comparable readObject(@NotNull ByteArrayInputStream stream) {
        final long l = BindingUtils.readLong(stream);
        final int i = BindingUtils.readInt(stream);
        return Instant.ofEpochSecond(l, i);
    }

    @Override
    public void writeObject(@NotNull LightOutputStream output, @NotNull Comparable object) {
        final Instant instant = (Instant) object;

        output.writeUnsignedLong(instant.getEpochSecond() ^ 0x8000000000000000L);
        output.writeUnsignedInt(instant.getNano() ^ 0x80000000);

    }






}
