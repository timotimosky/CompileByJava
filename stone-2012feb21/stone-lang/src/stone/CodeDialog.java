package stone;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*CodeDialog 对象是 Lexer 类的构造函数中的参数。
 * CodeDialog 是 java.io.Reader 的子类。
 * Lexer 在调用 read 方法从该对象中读取字符时，
 * 界面上将显示一个对话框，用户输入的文本将成为 read 方法的返回值。
 * 如果上一次显示对话框时输入的文本没有被删除，
 * 这些文本将首先被返回。用户点击对话框的取消按钮后，输入结束。*/
public class CodeDialog extends Reader {
    private String buffer = null;
    private int pos = 0;

    public int read(char[] cbuf, int off, int len) throws IOException {
        if (buffer == null) {
            String in = showDialog();
            if (in == null)
                return -1;
            else {
                print(in);
                buffer = in + "\n";  
                pos = 0;
            }
        }

        int size = 0;
        int length = buffer.length();
        while (pos < length && size < len)
            cbuf[off + size++] = buffer.charAt(pos++);
        if (pos == length)
            buffer = null;
        return size;
    }
    protected void print(String s) { System.out.println(s); }
    public void close() throws IOException {}
    protected String showDialog() {
        JTextArea area = new JTextArea(20, 40);
        JScrollPane pane = new JScrollPane(area);
        int result = JOptionPane.showOptionDialog(null, pane, "Input",
                                                  JOptionPane.OK_CANCEL_OPTION,
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  null, null, null);
        if (result == JOptionPane.OK_OPTION)
            return area.getText();
        else
            return null;
    }
    public static Reader file() throws FileNotFoundException {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return new BufferedReader(new FileReader(chooser.getSelectedFile()));
        else
            throw new FileNotFoundException("no file specified");
    }
}
