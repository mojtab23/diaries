package io.github.mojtab23.diaries.binding;

import io.github.mojtab23.diaries.model.diary.KeyEntry;
import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.bindings.ComparableBinding;
import jetbrains.exodus.util.LightOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

/**
 * Created by mojtab23 on 6/5/2017.
 */
public class KeyEntryBinding extends ComparableBinding {

    public static final KeyEntryBinding BINDING = new KeyEntryBinding();

    public static KeyEntry entryToKeyEntry(@NotNull final ByteIterable entry) {
        return (KeyEntry) BINDING.entryToObject(entry);
    }

    public static ArrayByteIterable keyEntryToEntry(final KeyEntry object) {
        return BINDING.objectToEntry(object);
    }

    @Override
    public Comparable readObject(@NotNull ByteArrayInputStream stream) {
        final DataInputStream inputStream = new DataInputStream(stream);
        return KeyEntry.readKeyEntry(inputStream);
    }

    @Override
    public void writeObject(@NotNull LightOutputStream output, @NotNull Comparable object) {
        final KeyEntry keyEntry = (KeyEntry) object;
        output.write(keyEntry.toByteArray());
    }


}
