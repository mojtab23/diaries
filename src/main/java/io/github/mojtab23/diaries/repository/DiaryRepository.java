package io.github.mojtab23.diaries.repository;

import io.github.mojtab23.diaries.InstantBinding;
import io.github.mojtab23.diaries.model.diary.Diary;
import jetbrains.exodus.entitystore.*;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mojtab23 on 5/5/2017.
 */

@Repository
public class DiaryRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiaryRepository.class);

    private final PersistentEntityStore entityStore;
    private final String entityType = "Diary";

    public DiaryRepository() {
        entityStore = PersistentEntityStores.newInstance(".DiariesAppData");


    }


    public void saveDiary(final Diary diary) {
        entityStore.executeInTransaction((StoreTransaction txn) -> {
            try {
                entityStore.registerCustomPropertyType(txn, Instant.class, InstantBinding.BINDING);
            } catch (EntityStoreException ignored) {
                // TODO: 5/23/2017 should update xodus to 1.0.6
            }
            diaryToEntity(diary, txn);
        });

    }

    public Entity diaryToEntity(final Diary diary, final StoreTransaction txn) {

        final Entity diaryEntity = txn.newEntity(entityType);
        diaryEntity.setProperty("text", diary.getText());

        diaryEntity.setProperty("timestamp", diary.getTimestamp());
        return diaryEntity;
    }


    public Diary entityToDiary(final Entity entity, final StoreTransaction txn) {


        final @Nullable String text = (String) entity.getProperty("text");
        final @Nullable Instant timestamp = (Instant) entity.getProperty("timestamp");
//        final ByteIterable timestampRow = entity.getRawProperty("timestamp");

        if (text != null) {
            if (timestamp/*Row*/ != null) {
//                final Instant timestamp = InstantBinding.entryToInstant(timestampRow);
                return new Diary(text, timestamp);
            } else
                return new Diary(text, Instant.MIN);
        }
        return null;
    }

    public List<Diary> readAllDiaries() {

        return entityStore.computeInReadonlyTransaction(txn -> {
            try {
                entityStore.registerCustomPropertyType(txn, Instant.class, InstantBinding.BINDING);
            } catch (EntityStoreException ignored) {
                // TODO: 5/23/2017 should update xodus to 1.0.6
            }


            final EntityIterable allDiaries = txn.getAll(entityType);
//            final long size = allDiaries.size();
//            if ((size < 1)) {
//                return Collections.EMPTY_LIST;
//            } else {
            final List<Diary> diaryList = new ArrayList<>();
            allDiaries.forEach(entity -> {
                final Diary e = entityToDiary(entity, txn);
                if (e != null) {
                    diaryList.add(e);
                }
            });
            LOGGER.warn("Number of Diaries: " + diaryList.size());
            return diaryList;

        });

    }

    public void deleteAll() {

        entityStore.executeInTransaction(txn -> {


            txn.getAll(entityType).forEach(entity ->
            {
                if (!entity.delete()) {
                    LOGGER.warn("cant delete {}", entityToDiary(entity, txn));
                }
            });
        });

    }


    @PreDestroy
    public void atEnd() {
        entityStore.close();
    }

}
