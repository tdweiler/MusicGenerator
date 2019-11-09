import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;

/**
 * A procedural generation project that creates piano music in Java.
 * Based off of the work found at: https://gist.github.com/pbloem/d29bf80e69d333415622
 */
public class MusicGenerator {

    private static List<String> notes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");

    // Scales
    private static List<String> C_Major = Arrays.asList("C", "D", "E", "F", "G", "A", "B", "C");
    private static List<String> CSharp_Major = Arrays.asList("C#", "D#", "E#", "F#", "G#", "A#", "B#", "C#");
    private static List<String> D_Major = Arrays.asList("D", "E", "F#", "G", "A", "B", "C#", "D");
    private static List<String> EFlat_Major = Arrays.asList("Eb", "F", "G", "Ab", "Bb", "C", "D", "Eb");

    private static List<String> inputScale;
    private static String inputScaleString;
    private static String inputOctave;

    private static MidiChannel[] channels;
    private static int INSTRUMENT = 0; // 0 is a piano, 9 is percussion
    private static int VOLUME = 80; // between 0 et 127

    public static void main( String[] args ) {

        try {

            // Initialize input
            inputOctave = args[0];
            inputScaleString = args[1];
            int sectionDuration = Integer.parseInt(args[2]);
            int numVerses = Integer.parseInt(args[3]);

            switch (inputScaleString) {
                case "CM":
                    inputScale = CSharp_Major;
                    break;
                case "C#M":
                    inputScale = CSharp_Major;
                    break;
                case "DM":
                    inputScale = D_Major;
                    break;
            }

            // Open a synthesizer
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();

            // Play the music piece

            for(int i = 0; i < inputScale.size() - 2; i ++) {
                play(inputOctave + inputScale.get(i), 1);
            }
            int nextOctave = Integer.parseInt(inputOctave) + 1;
            String nextOctaveString = Integer.toString(nextOctave);
            play(nextOctaveString + inputScale.get(inputScale.size() - 2), 1);
            play(nextOctaveString + inputScale.get(inputScale.size() - 1), 1);

            //playPiece(inputOctave + inputScale.get(0), sectionDuration, numVerses);

            // finish up
            synth.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Plays the given note for the given duration
     */
    private static void play(String note, double duration) throws InterruptedException
    {
        // Convert duration to milliseconds
        int d = (int) (duration * 1000);

        // Start playing a note
        channels[INSTRUMENT].noteOn(id(note), VOLUME);

        // Duration of the note
        Thread.sleep(d);

        // Stop playing a note
        channels[INSTRUMENT].noteOff(id(note));
    }

    /**
     * Plays nothing for the given duration
     */
    private static void rest(int duration) throws InterruptedException
    {
        // Convert duration to seconds
        duration = duration * 1000;

        // Duration of the rest
        Thread.sleep(duration);
    }

    /**
     * Returns the MIDI id for a given note: eg. 4C -> 60
     * @return correct id
     */
    private static int id(String note)
    {
        int octave = Integer.parseInt(note.substring(0, 1));
        String string = note.substring(1);
        int hello = notes.indexOf(note.substring(1)) + 12 * octave + 12;
        System.out.println(hello + " " + string);
        return notes.indexOf(note.substring(1)) + 12 * octave + 12;
    }

    /**
     * Determines the current duration of the piece and plays the current note
     */
    private static void playPiece(String startingNote, int sectionDuration, int numVerses) throws InterruptedException {
        List<Note> song = generateSong(startingNote, sectionDuration, numVerses);

        // Loop through each note in the array
        for (Note note : song) {
            //System.out.println(note.getNote() + " " + note.getLength());
            play(note.getNote(), note.getLength());
        }
    }

    private static List<Note> generateSong(String startingNote, int sectionDuration, int numVerses) {
        List<Note> song = new ArrayList<>();

        // Get random note from appropriate key
        List<Note> intro = generateSection(startingNote, 15);
        song.addAll(intro);

        // Get random note from appropriate key
        List<Note> chorus = generateSection(song.get(song.size() - 1).getNote(), sectionDuration);

        for (int i = 0; i < numVerses; i++) {
            List<Note> verse = generateSection(song.get(song.size() - 1).getNote(), sectionDuration);
            song.addAll(verse);
            song.addAll(chorus);
        }

        List<Note> outro = generateSection(song.get(song.size() - 1).getNote(), 15);
        song.addAll(outro);

        return song;
    }

    private static List<Note> generateSection(String startingNote, int sectionDuration) {
        List<Note> section = new ArrayList<>();
        double noteDurationTotal;

        String nextNote = startingNote;
        double currentNoteDuration = noteDurationTotal = getRandomDouble(0.5, 1.5);
        Note currentNote = new Note(nextNote, currentNoteDuration);
        section.add(currentNote);

        while (noteDurationTotal < sectionDuration) {
            nextNote = generateNextNote(currentNote.getNote());
            currentNoteDuration = getRandomDouble(0.5, 1.5);
            section.add(new Note(nextNote, currentNoteDuration));

            noteDurationTotal = noteDurationTotal + currentNoteDuration;
        }

        return section;
    }

    private static double getRandomDouble(double min, double max){
        double x = (Math.random()*((max-min)))+min;
        return x;
    }

    /**
     * Generates the next note
     * @return currentNote
     */
    private static String generateNextNote(String currentNote) {
        double rnd = Math.random();

        // Frequency table mapping the probability of any given note following after the current note
            if (currentNote.equals(inputOctave + inputScale.get(0))) {
                if (rnd <= 0.2)
                    currentNote = inputOctave + inputScale.get(0);
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = inputOctave + inputScale.get(1);
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = inputOctave + inputScale.get(2);
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = inputOctave + inputScale.get(3);
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = inputOctave + inputScale.get(4);
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = inputOctave + inputScale.get(5);
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = inputOctave + inputScale.get(6);
                else
                    currentNote = inputOctave + inputScale.get(7);
            } else if (currentNote.equals(inputOctave + inputScale.get(1))) {
                if (rnd <= 0.2)
                    currentNote = inputOctave + inputScale.get(0);
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = inputOctave + inputScale.get(1);
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = inputOctave + inputScale.get(2);
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = inputOctave + inputScale.get(3);
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = inputOctave + inputScale.get(4);
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = inputOctave + inputScale.get(5);
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = inputOctave + inputScale.get(6);
                else
                    currentNote = inputOctave + inputScale.get(7);
            } else if (currentNote.equals(inputOctave + inputScale.get(2))) {
                if (rnd <= 0.2)
                    currentNote = inputOctave + inputScale.get(0);
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = inputOctave + inputScale.get(1);
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = inputOctave + inputScale.get(2);
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = inputOctave + inputScale.get(3);
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = inputOctave + inputScale.get(4);
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = inputOctave + inputScale.get(5);
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = inputOctave + inputScale.get(6);
                else
                    currentNote = inputOctave + inputScale.get(7);
            } else if (currentNote.equals(inputOctave + inputScale.get(3))) {
                if (rnd <= 0.2)
                    currentNote = inputOctave + inputScale.get(0);
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = inputOctave + inputScale.get(1);
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = inputOctave + inputScale.get(2);
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = inputOctave + inputScale.get(3);
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = inputOctave + inputScale.get(4);
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = inputOctave + inputScale.get(5);
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = inputOctave + inputScale.get(6);
                else
                    currentNote = inputOctave + inputScale.get(7);
            } else if (currentNote.equals(inputOctave + inputScale.get(4))) {
                if (rnd <= 0.2)
                    currentNote = inputOctave + inputScale.get(0);
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = inputOctave + inputScale.get(1);
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = inputOctave + inputScale.get(2);
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = inputOctave + inputScale.get(3);
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = inputOctave + inputScale.get(4);
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = inputOctave + inputScale.get(5);
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = inputOctave + inputScale.get(6);
                else
                    currentNote = inputOctave + inputScale.get(7);
            } else if (currentNote.equals(inputOctave + inputScale.get(5))) {
                if (rnd <= 0.2)
                    currentNote = inputOctave + inputScale.get(0);
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = inputOctave + inputScale.get(1);
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = inputOctave + inputScale.get(2);
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = inputOctave + inputScale.get(3);
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = inputOctave + inputScale.get(4);
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = inputOctave + inputScale.get(5);
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = inputOctave + inputScale.get(6);
                else
                    currentNote = inputOctave + inputScale.get(7);
            } else if (currentNote.equals(inputOctave + inputScale.get(6))) {
                if (rnd <= 0.2)
                    currentNote = inputOctave + inputScale.get(0);
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = inputOctave + inputScale.get(1);
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = inputOctave + inputScale.get(2);
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = inputOctave + inputScale.get(3);
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = inputOctave + inputScale.get(4);
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = inputOctave + inputScale.get(5);
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = inputOctave + inputScale.get(6);
                else
                    currentNote = inputOctave + inputScale.get(7);
            } else if (currentNote.equals(inputOctave + inputScale.get(7))) {
                if (rnd <= 0.2)
                    currentNote = inputOctave + inputScale.get(0);
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = inputOctave + inputScale.get(1);
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = inputOctave + inputScale.get(2);
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = inputOctave + inputScale.get(3);
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = inputOctave + inputScale.get(4);
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = inputOctave + inputScale.get(5);
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = inputOctave + inputScale.get(6);
                else
                    currentNote = inputOctave + inputScale.get(7);
            }
        return currentNote;
    }
}