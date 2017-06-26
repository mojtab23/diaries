//package io.github.mojtab23.diaries.repository;
//
//import io.github.mojtab23.diaries.model.diary.Diary;
//import io.github.mojtab23.diaries.model.diary.Id;
//import io.github.mojtab23.diaries.service.CipherService;
//import io.github.mojtab23.diaries.service.DiaryService;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.time.Instant;
//import java.util.List;
//
///**
// * Created by mojtab23 on 6/6/2017.
// */
//
//
////@RunWith(SpringRunner.class)
////@SpringBootTest
////@Import(SpringConfig.class)
//public class DiaryRepositoryTest {
//
//
//    //    @Autowired
//    DiaryRepository diaryRepository
//            = new DiaryRepository(new DiaryService(new CipherService()));
//
//
//    @Before
//    public void setUp() throws Exception {
//        diaryRepository.init();
//    }
//
//    @Test
//    public void testRepo() throws Exception {
//        final Diary diary1 = new Diary("diary one int test.", Instant.now());
//        final Diary diary2 = new Diary("diary two int test.", Instant.now());
//        diary2.setId(new Id());
//        System.out.println("######### before save #########");
//        System.out.println(diary1);
//        System.out.println(diary2);
//
//        diaryRepository.saveNewDiary(diary1);
//        diaryRepository.saveNewDiary(diary2);
//        System.out.println("#########    saved   #########");
//
//        final List<Diary> diaries = diaryRepository.readAllDiaries();
//        System.out.println("######### read diaries #########");
//        for (Diary diary : diaries) {
//            System.out.println(diary);
//        }
//
//
//    }
//}