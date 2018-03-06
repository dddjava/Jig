package jig.shell;

import jig.domain.model.jdeps.AnalysisTarget;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class JigCommandsTest {

    ShellApplication application = new ShellApplication();
    JigCommands sut = initJigCommands();

    private JigCommands initJigCommands() {
        return new JigCommands(
                application.relationAnalyzer(),
                application.getDiagramService());
    }

    @Test
    void jdeps() {
        sut.jdeps("../sut", "sut.*", AnalysisTarget.PACKAGE);
        // エラーにならなければOK
    }

    @Test
    void packageDiagram() throws Exception {
        File file = new File(Files.newTemporaryFolder(), "temp.png");
        file.deleteOnExit();
        sut.packageDiagram("../sut", "sut.*", file.toString());

        // 出力対象がない場合のPNGファイルが411byteなのでそれ以上で
        assertThat(file.length()).isGreaterThan(411);
    }
}