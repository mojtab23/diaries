package io.github.mojtab23.diaries.repository;

import io.github.mojtab23.diaries.binding.ArrayByteIterableBinding;
import io.github.mojtab23.diaries.model.diary.Diary;
import io.github.mojtab23.diaries.service.DiaryService;
import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.entitystore.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mojtab23 on 5/5/2017.
 */

@Repository
public class DiaryRepository1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiaryRepository1.class);

    private final PersistentEntityStore entityStore;
    private final DiaryService diaryService;

    @Autowired
    public DiaryRepository1(DiaryService diaryService) {
        entityStore = PersistentEntityStores.newInstance(".DiariesAppData");
        this.diaryService = diaryService;
    }


    public void saveDiary(final Diary diary) {
        entityStore.executeInTransaction((StoreTransaction txn) -> {
            registerCustomTypes(txn);
            diaryService.diaryToEntity(diary, txn);
        });

    }

    @NotNull
    public List<Diary> readAllDiaries() {

        return entityStore.computeInReadonlyTransaction(txn -> {
            registerCustomTypes(txn);

            final EntityIterable allDiaries = txn.getAll(Diary.ENTITY_TYPE);
//            final long size = allDiaries.size();
//            if ((size < 1)) {
//                return Collections.EMPTY_LIST;
//            } else {
            final List<Diary> diaryList = new ArrayList<>();
            allDiaries.forEach(entity -> {
                final Diary e = diaryService.entityToDiary(entity);
                if (e != null) {
                    diaryList.add(e);
                }
            });
            LOGGER.debug("Number of Diaries: " + diaryList.size());
            return diaryList;

        });

    }

    public void deleteAll() {

        entityStore.executeInTransaction(txn -> {

            registerCustomTypes(txn);
            final EntityIterable entities = txn.getAll(Diary.ENTITY_TYPE);
            long count = 0;
            for (Entity entity : entities) {
                if (!entity.delete()) {
                    LOGGER.warn("cant delete {}", diaryService.entityToDiary(entity));
                } else count++;
            }
            LOGGER.info("{} Diaries deleted!", count);

        });

    }


    private void registerCustomTypes(StoreTransaction txn) {
        try {
//                entityStore.registerCustomPropertyType(txn, Instant.class, InstantBinding.BINDING);
            entityStore.registerCustomPropertyType(txn, ArrayByteIterable.class, ArrayByteIterableBinding.BINDING);

        } catch (EntityStoreException ignored) {
            // TODO: 5/23/2017 should update xodus to 1.0.6
        }

    }

    @PreDestroy
    public void atEnd() {
        entityStore.close();
    }

}
