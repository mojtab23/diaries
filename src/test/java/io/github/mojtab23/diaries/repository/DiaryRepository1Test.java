package io.github.mojtab23.diaries.repository;

import io.github.mojtab23.diaries.model.diary.Diary;
import io.github.mojtab23.diaries.service.CipherService;
import io.github.mojtab23.diaries.service.DiaryService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

/**
 * Created by mojtab23 on 6/26/2017.
 */
public class DiaryRepository1Test {


    private DiaryService diaryService;
    private DiaryRepository1 diaryRepository;

    @Before
    public void setUp() throws Exception {
        final CipherService cipherService = new CipherService();
        cipherService.init();
        byte[] key = "Bar12345Bar12345".getBytes("UTF-8");
        cipherService.setKey(key);
        diaryService = new DiaryService(cipherService);
        diaryRepository = new DiaryRepository1(diaryService);
    }


    @Test
    public void testSave() throws Exception {
        diaryRepository.saveDiary(new Diary("salam1", Instant.now()));


    }

    @Test
    public void testReadAll() throws Exception {
        final List<Diary> diaries = diaryRepository.readAllDiaries();
        System.out.println("Diaries:");
        for (Diary diary : diaries) {
            System.out.println("    "+diary);
        }
    }

    @Test
    public void testDeleteAll() throws Exception {
        diaryRepository.deleteAll();
    }

    @After
    public void tearDown() throws Exception {
        diaryRepository.atEnd();
    }
}