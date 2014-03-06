package test;

import db.DatabaseHandler;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: FF63
 * Date: 3/5/14
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHandlerTest {
    private DatabaseHandler db;

    public DatabaseHandlerTest(){
        db = new DatabaseHandler();
    }

    @Test
    public void testAuthenticate() throws Exception {
        assert(db.authenticate("Bob","hei"));
        assert(db.authenticate("WRONG", "WRONG") == false);
    }

}
