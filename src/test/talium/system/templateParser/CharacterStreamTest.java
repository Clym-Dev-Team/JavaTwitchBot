package talium.system.templateParser;

import org.junit.jupiter.api.Test;

class CharacterStreamTest {

    @Test
    void peek_shows_next_character() {
        var stream = new CharakterStream("TEST STREAM");
        assert stream.peek() == 'T';
        assert stream.peek() == 'T';
        assert stream.peek() == 'T';
        assert stream.peek() == 'T';
    }

    @Test
    void next_advances_to_next_character() {
        var stream = new CharakterStream("TEST STREAM");
        assert stream.next() == 'T';
        assert stream.next() == 'E';
        assert stream.next() == 'S';
        assert stream.next() == 'T';
        assert stream.peek() == ' ';
        assert stream.peek() == ' ';
    }

    @Test
    void eof_true_at_end() {
        String testStream = "TEST STREAM";
        var stream = new CharakterStream(testStream);
        for (int i = 0; i < testStream.length() ; i++) {
            stream.next();
        }
        assert stream.isEOF();
    }

    @Test
    void eof_false_at_not_end() {
        var stream = new CharakterStream("TEST STREAM");
        assert !stream.isEOF();
        stream.next();
        assert !stream.isEOF();
        stream.next();
        assert !stream.isEOF();
    }
}
