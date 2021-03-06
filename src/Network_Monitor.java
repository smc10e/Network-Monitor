import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by smcampbell on 11/2/2015.
 */
public class Network_Monitor {

    private static long sum = 0;
    private static Boolean kb = false;

    public static void main(String[] args) throws InterruptedException {

        StringBuffer output;
        Process p;
        BufferedReader reader;
        String line, text, received;
        long oldVal = 0, newVal;
        Pattern pattern;
        Matcher matcher;

        //setting up the window to display the data
        JFrame frame = new JFrame();
        JTextArea textArea = new JTextArea();
        JTextArea textArea2 = new JTextArea();
        JLabel label = new JLabel();
        JLabel label2 = new JLabel();
        JButton button = new JButton("Toggle Kb");
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 200);
        label.setText("Bytes Received: ");
        label2.setText("Bytes received since program start: ");
        textArea.setEditable(false);
        textArea2.setEditable(false);
        frame.add(label);
        frame.add(textArea);
        frame.add(label2);
        frame.add(textArea2);
        long val = 0;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                kb = !kb;
            }
        };

        button.addActionListener(actionListener);
        frame.add(button);

        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        frame.addWindowListener(windowListener);

        while(true){
            output = new StringBuffer();

            //runs a shell script to get the current network statistics
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

            //uses a regex to find the number of bytes received that are
            //saved in the output String buffer
            pattern = Pattern.compile("(\\b(Bytes)\\b)(\\s*)(\\d*)");
            matcher = pattern.matcher(text);
            matcher.find();
            //group(4) refers to the 4th group of parentheses in the regex
            //gets the number of bytes received
            received = matcher.group(4);

            newVal = Long.parseLong(received);

            //computes number of bytes received since start of program
            if(oldVal>0) {
                sum += (newVal - oldVal);
                if(kb ==false) {
                    val = sum;
                }
                else{
                    val = sum/1024;
                }
            }
            oldVal = newVal;

            //clears the text area to "refresh" and the appends the received string
            textArea.setText(" ");
            textArea.append(received);
            textArea2.setText(" ");
            textArea2.append(String.valueOf(val));
            Thread.sleep(500);
        }
    }
}
