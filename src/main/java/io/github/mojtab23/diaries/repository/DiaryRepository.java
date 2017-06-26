package io.github.mojtab23.diaries.repository;

import io.github.mojtab23.diaries.binding.KeyEntryBinding;
import io.github.mojtab23.diaries.model.diary.Diary;
import io.github.mojtab23.diaries.model.diary.KeyEntry;
import io.github.mojtab23.diaries.service.DiaryService;
import io.github.mojtab23.diaries.util.Tuple;
import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mojtab23 on 6/4/2017.
 */

@Repository
public class DiaryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiaryRepository.class);

    private final DiaryService diaryService;
    private Environment env;
    private Store diaryStore;

    @Autowired
    public DiaryRepository(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostConstruct
    public void init() {
        env = Environments.newInstance(".DiariesData");
        openDiaryStore();

    }

    private void openDiaryStore() {

        diaryStore = env.computeInTransaction(txn ->
                env.openStore("Diary", StoreConfig.WITHOUT_DUPLICATES, txn));

    }

    public void saveNewDiary(@NotNull final Diary diary) {
//        final List<Tuple<KeyEntry, ArrayByteIterable>> tupleList = diaryService.diaryToTuple(diary, false);
//        env.executeInTransaction(txn -> {
//            for (Tuple<KeyEntry, ArrayByteIterable> tuple : tupleList) {
//                if (!diaryStore.add(txn, KeyEntryBinding.keyEntryToEntry(tuple.getA()), encrypt(tuple.getB()))) {
//                    LOGGER.error("duplicate key!");
//                }
//            }
//        });
    }


    public List<Diary> readAllDiaries() {
        return env.computeInTransaction(txn -> {

            try (final Cursor cursor = diaryStore.openCursor(txn)) {
                List<List<Tuple<KeyEntry, ByteIterable>>> objectList = new ArrayList<>();
                List<Tuple<KeyEntry, ByteIterable>> tupleList = new ArrayList<>();
                objectList.add(tupleList);
                KeyEntry lastKey = null;
                KeyEntry newKey;


                while (cursor.getNext()) {
                    newKey = KeyEntryBinding.entryToKeyEntry(cursor.getKey());
                    if (newKey != null) {
                        if (lastKey != null) {
                            if (!lastKey.getId().equals(newKey.getId())) {
                                tupleList = new ArrayList<>();
                                objectList.add(tupleList);
                            }
                        }
                        tupleList.add(new Tuple<>(newKey, cursor.getValue()));
                        lastKey = newKey;
                    } else {
                        LOGGER.error("error in reading key.");
                    }
                }

                final ArrayList<Diary> diaries = new ArrayList<>();
                for (List<Tuple<KeyEntry, ByteIterable>> tuple : objectList) {
//                    final Diary diary = diaryService.tupleToDiary(tuple);
//                    diaries.add(diary);
                }
                return diaries;
            } catch (Exception e) {
                LOGGER.error("Can't parse diary from database.", e);
                e.printStackTrace();
            }
            return null;
        });
    }

    private ByteIterable encrypt(ByteIterable row) {
        // TODO: 6/4/2017 impl encryption
        return row;
    }

    private ByteIterable decrypt(ByteIterable row) {
        // TODO: 6/4/2017 impl encryption
        return row;
    }


}
