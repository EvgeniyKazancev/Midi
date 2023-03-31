import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BeetBox {
    JPanel mainPanel;
    ArrayList<JCheckBox> checkBoxes;
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    JFrame theFrame;
    String[] nameInstrument = {"bass Drum", "Closed Hi-Hat", "open Hi-Hat", "Acoustic Share", "Crash Cymbal", "Hand Clap", "High Tom", "Hi bongo",
            "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap", "Low-mid Tom", "High Agogo", "Open Hi Conga"};// Это название инструментов
    int[] instrument = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63}; // Это ноты

    public static void main(String[] args) {
        new BeetBox().buildGui();
    }

    public void buildGui() {
        theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //Пустая граница позволяет создать поля между краями панели

        checkBoxes = new ArrayList<JCheckBox>();
        Box buttonBox = new Box(BoxLayout.Y_AXIS);

        JButton start = new JButton("Start");
        start.addActionListener(new); // добавить метод позже в скобки!
        buttonBox.add(start);

        JButton stop = new JButton("Stop");
        stop.addActionListener(new); // сюда тоже
        buttonBox.add(stop);

        JButton tempoU = new JButton("Tempo Up");
        tempoU.addActionListener(new); //и сюда
        buttonBox.add(tempoU);

        JButton tempoD = new JButton("Tempo Down");
        tempoD.addActionListener(new); // и тут
        buttonBox.add(tempoD);
        Box nameBox = new Box(BoxLayout.Y_AXIS);
        for (int i = 0; i < 16; i++) {
            nameBox.add(new Label(nameInstrument[i]));
        }
        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);

        theFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16, 16);
        grid.setVgap(1);
        grid.setHgap(2);
        mainPanel = new JPanel(grid);
        background.add(BorderLayout.CENTER, mainPanel);

        for (int i = 0; i < 256; i++) {            //Создаем флажки и складываем в ArrayList ставя им false
            JCheckBox c = new JCheckBox();
            c.setSelected(false);
            checkBoxes.add(c);
            mainPanel.add(c);

        }
        theFrame.setBounds(50, 50, 300, 300);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    public void setUpMidi() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
            sequencer.setTempoInBPM(120);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buildTrackAndStart() {
        int[] trackList = null;
        sequence.deleteTrack(track);
        track = sequence.createTrack();

        for (int i = 0; i < 16; i++) {
            trackList = new int[16];
            int key = instrument[i];  // клавиша которая представляет инструмент
            for (int j = 0; j < 16; j++) {
                JCheckBox jc = (JCheckBox) checkBoxes.get(j + (16 * i));
                if (jc.isSelected()) {
                    trackList[j] = key;
                } else {
                    trackList[j] = 0;
                }
            }
            makeTracks(trackList);
            track.add(makeEvent(176, 1, 127, 0, 16));
        }
        track.add(makeEvent(192, 9, 1, 0, 15));
        try {
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (InvalidMidiDataException e) {
           e.printStackTrace();
        }

    }

}
