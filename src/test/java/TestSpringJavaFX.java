import io.github.mojtab23.diaries.DiariesView;
import io.github.mojtab23.diaries.repository.DiaryRepository1;
import io.github.mojtab23.diaries.service.CipherService;
import io.github.mojtab23.diaries.service.DiaryService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by mojtab23 on 5/13/2017.
 */
public class TestSpringJavaFX extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();
        Scene scene = new Scene(root, 500, 400);
        scene.getStylesheets().add(this.getClass().getResource("/style.css").toExternalForm());

//        final JFXButton control1 = new JFXButton("alo1");
//        control1.setButtonType(JFXButton.ButtonType.RAISED);
//        final Button control2 = new Button("alo2");
//        final Button control3 = new Button("alo3");
//        root.getChildren().add(control1);// TODO: 5/6/2017 create new view
//        root.getChildren().add(new JFXRippler(control2));// TODO: 5/6/2017 create new view
//        root.getChildren().add(new JFXRippler(control3));// TODO: 5/6/2017 create new view
        final CipherService cipherService = new CipherService();
        cipherService.init();
        byte[] key = "Bar12345Bar12345".getBytes("UTF-8");
        cipherService.setKey(key);
        DiaryService diaryService = new DiaryService(cipherService);
        final DiariesView diariesView = new DiariesView(new DiaryRepository1(diaryService));
        diariesView.init();
        root.getChildren().add(diariesView);


        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
