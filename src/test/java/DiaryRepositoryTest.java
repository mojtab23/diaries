import io.github.mojtab23.diaries.repository.DiaryRepository;
import io.github.mojtab23.diaries.model.diary.Diary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;


/**
 * Created by mojtab23 on 5/5/2017.
 */
public class DiaryRepositoryTest {


    private DiaryRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = new DiaryRepository();
    }

    @Test
    public void saveDiary() throws Exception {
        Diary diary = new Diary();
        diary.setText("diary there");
        diary.setTimestamp(Instant.now());
        repository.saveDiary(diary);

    }


    @Test
    public void readAllDiaries() throws Exception {

        System.out.println("All Diaries:");
        repository.readAllDiaries().forEach(System.out::println);

    }

//    @Test
//    public void deleteAll() throws Exception {
//        repository.deleteAll();
//    }

    @After
    public void tearDown() throws Exception {
        repository.atEnd();
    }
}