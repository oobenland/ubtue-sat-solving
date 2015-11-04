package satsolving;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DimacsParserTest {

    @Test
    public void testRead() throws Exception {
        new DimacsParser().read(new ByteArrayInputStream(("c This is a comment\n" +
                "c This is another comment\n" +
                "p cnf 6 4\n" +
                "1 -2 3 0\n" +
                "2 -4 5 0\n" +
                "-4 0\n").getBytes()));
    }

    @Test
    public void testAim100() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/aim-100-1_6-no-1.cnf"});
    }

    @Test
    public void testAim200() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/aim-200-2_0-yes1-2.cnf"});
    }

    @Test
    public void testBarrel5() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/barrel5-no.cnf"});
    }

    @Test
    public void testGoldB() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/goldb-heqc-k2mul.cnf"});
    }

    @Test
    public void testHanoi() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/hanoi4-yes.cnf"});
    }
    @Test
    public void testHole8() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/hole8-no.cnf"});
    }
    @Test
    public void testLongMult6() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/longmult6-no.cnf"});
    }
    @Test
    public void testMiza() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/miza-sr06-md5-47-03.cnf"});
    }
    @Test
    public void testSSA7552() throws IOException {
        DimacsParser.main(new String[]{"tests/resources/sat_instances/ssa7552-160-yes.cnf"});
    }
}