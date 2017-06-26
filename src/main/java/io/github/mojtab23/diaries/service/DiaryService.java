package io.github.mojtab23.diaries.service;

import io.github.mojtab23.diaries.binding.InstantBinding;
import io.github.mojtab23.diaries.model.diary.Diary;
import io.github.mojtab23.diaries.model.diary.Id;
import io.github.mojtab23.diaries.model.diary.KeyEntry;
import io.github.mojtab23.diaries.util.Tuple;
import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.bindings.StringBinding;
import jetbrains.exodus.entitystore.Entity;
import jetbrains.exodus.entitystore.StoreTransaction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mojtab23 on 6/4/2017.
 */

@Service
public class DiaryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiaryService.class);
    private final CipherService cipherService;
    private final String textProperty = "text";
    private final String timestamp = "timestamp";

    @Autowired
    public DiaryService(CipherService cipherService) {
        this.cipherService = cipherService;
    }


//    public List<Tuple<KeyEntry, ArrayByteIterable>> diaryToTuple(@NotNull final Diary diary, final boolean hasId) {
//        final ArrayList<Tuple<KeyEntry, ArrayByteIterable>> tuple = new ArrayList<>();
//        final Id id;
//        if (hasId) {
//            if ((id = diary.getId()) == null) {
//                throw new IllegalArgumentException("It has no Id: " + diary.toString());
//            }
//        } else {
//            id = new Id();
//        }
//
//
//        tuple.add(buildTimestampTuple(diary, id));
//        tuple.add(buildTextTuple(diary, id));
//
//        return tuple;
//
//    }
//
//    @NotNull
//    private Tuple<KeyEntry, ArrayByteIterable> buildTextTuple(@NotNull Diary diary, Id id) {
//        final KeyEntry key = new KeyEntry(id, 1);
//        final ArrayByteIterable arrayByteIterable = StringBinding.stringToEntry(diary.getText());
//        return new Tuple<>(key, arrayByteIterable);
//    }
//
//    @NotNull
//    private Tuple<KeyEntry, ArrayByteIterable> buildTimestampTuple(@NotNull Diary diary, Id id) {
//        final KeyEntry key = new KeyEntry(id, 0);
//        final ArrayByteIterable instantValue = InstantBinding.instantToEntry(diary.getTimestamp());
//        return new Tuple<>(key, instantValue);
//    }
//
//
//    public Diary tupleToDiary(List<Tuple<KeyEntry, ArrayByteIterable>> tupleList) throws Exception {
//        final Diary diary = new Diary();
//        for (Tuple<KeyEntry, ArrayByteIterable> tuple : tupleList) {
//            final KeyEntry keyEntry = tuple.getA();
//            final Id id = keyEntry.getId();
//            if (diary.getId() == null) {
//                diary.setId(id);
//            } else {
//                if (!diary.getId().equals(id)) throw new Exception("miss match ids");
//            }
//            final ByteIterable value = tuple.getB();
//            final int number = keyEntry.getFieldNumber();
//            switch (number) {
//                case 0: {
//                    diary.setTimestamp(InstantBinding.entryToInstant(value));
//                    break;
//                }
//                case 1: {
//                    diary.setText(StringBinding.entryToString(value));
//                    break;
//                }
//                default: {
//                    LOGGER.error("undefined field number:{}", number);
//                    break;
//                }
//            }
//
//        }
//        return diary;
//    }


    public void diaryToEntity(final Diary diary, final StoreTransaction txn) {

        final Entity diaryEntity = txn.newEntity(Diary.ENTITY_TYPE);

        setEntityProperty(diaryEntity, textProperty, StringBinding.stringToEntry(diary.getText()));
        setEntityProperty(diaryEntity, timestamp, InstantBinding.instantToEntry(diary.getTimestamp()));

    }

    public Diary entityToDiary(final Entity entity) {
        final ArrayByteIterable rowText = decryptProperty(entity, textProperty);
        final ArrayByteIterable rowTimestamp = decryptProperty(entity, timestamp);

        final String text = StringBinding.entryToString(rowText);
        final Instant instant = InstantBinding.entryToInstant(rowTimestamp);

        return new Diary(text, instant,entity.getId());
    }

    private ArrayByteIterable decryptProperty(Entity entity, String propertyName) {
        final byte[] iv = buildIV(entity, propertyName);
        final ArrayByteIterable raw = (ArrayByteIterable) entity.getProperty(propertyName);
        return cipherService.decrypt(raw, iv);
    }

    private void setEntityProperty(Entity entity, String propertyName, ArrayByteIterable propertyValue) {
        final byte[] iv = buildIV(entity, propertyName);
        entity.setProperty(propertyName, cipherService.encrypt(propertyValue, iv));
    }


    @NotNull
    private byte[] buildIV(Entity entity, String text) {
        return ByteBuffer.allocate(16).
                putInt(entity.getId().getTypeId()).
                putLong(entity.getId().getLocalId()).
                putInt(text.hashCode()).array();
    }

}
