import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by smcampbell on 11/2/2015.
 */
public class Network_Monitor {

    public static void main(String[] args) throws InterruptedException {

        StringBuffer output;
        Process p;
        BufferedReader reader;
        String line, text, received;
        Pattern pattern;
        Matcher matcher;

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JTextArea textArea = new JTextArea();
        JLabel label = new JLabel();
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setSize(200, 100);
        frame.add(panel);
        label.setText("Received Data: ");
        textArea.setEditable(false);
        panel.add(label);
        panel.add(textArea);
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(windowListener);

        while(true){
            output = new StringBuffer();
            try {
                p = Runtime.getRuntime().exec("netstat -e");
                p.waitFor();
                reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while((line = reader.readLine()) != null){
                    output.append(line + "\n");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            text = output.toString();

            pattern = Pattern.compile("(\\b(Bytes)\\b)(\\s*)(\\d*)");
            matcher = pattern.matcher(text);
            matcher.find();
            received = matcher.group(4);

            textArea.setText(" ");
            textArea.append(received);
            Thread.sleep(500);
        }
    }
}
