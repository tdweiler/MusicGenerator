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


    private static MidiChannel[] channels;
    private static int INSTRUMENT = 0; // 0 is a piano, 9 is percussion
    private static int VOLUME = 80; // between 0 et 127

    public static void main( String[] args ) {

        try {

            // Initialize input
            String startingNote = args[0];
            int sectionDuration = Integer.parseInt(args[1]);

            // Open a synthesizer
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();

            // Play the music piece

            for (int i = 0; i < D_Major.size() - 1; i++) {
                play("5" + D_Major.get(i), 1);
            }
            play("6" + D_Major.get(D_Major.size() - 1), 1);
            //playPiece(startingNote, sectionDuration);

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
    private static void play(String note, int duration) throws InterruptedException
    {
        // Convert duration to seconds
        duration = duration * 1000;

        // Start playing a note
        channels[INSTRUMENT].noteOn(id(note), VOLUME);

        // Duration of the note
        Thread.sleep(duration);

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
        return C_Major.indexOf(note.substring(1)) + 12 * octave + 12;
    }

    /**
     * Determines the current duration of the piece and plays the current note
     */
    private static void playPiece(String startingNote, int sectionDuration) throws InterruptedException {
        // Always start the song with the first note of duration one
        // int noteDurationTotal = 1;
        int currentNoteDuration = 1;

        List<String> song = generateSong(startingNote, sectionDuration);

        // Determine whether or not the song is over
        for (int i = 0; i < song.size(); i++) {

            // Play the next note
            play(song.get(i), currentNoteDuration);

            // Calculate the total duration of the song currently
            // noteDurationTotal = noteDurationTotal + currentNoteDuration;
        }
    }

    private static List<String> generateSong(String startingNote, int sectionDuration) {
        List<String> song = new ArrayList<>();
        String currentNote = startingNote;
        song.add(currentNote);

        for (int i = 1; i < sectionDuration; i++) {
            currentNote = generateNextNote(currentNote);
            song.add(currentNote);
        }

        return song;
    }

    /**
     * Generates the next note
     * @return currentNote
     */
    private static String generateNextNote(String currentNote) {
        double rnd = Math.random();

        // Frequency table mapping the probability of any given note following after the current note
        switch (currentNote) {
            case "6D":
                if (rnd <= 0.2)
                    currentNote = "6C";
                else if (rnd > 0.2 && rnd <= 0.4)
                    currentNote = "6D";
                else if (rnd > 0.4 && rnd <= 0.6)
                    currentNote = "6E";
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = "6F";
                else if (rnd > 0.7 && rnd <= 0.8)
                    currentNote = "6G";
                else if (rnd > 0.8 && rnd <= 0.9)
                    currentNote = "6A";
                else if (rnd > 0.9)
                    currentNote = "6B";
                break;
            case "6C":
                if (rnd <= 0.1)
                    currentNote = "6C";
                else if (rnd > 0.1 && rnd <= 0.3)
                    currentNote = "6D";
                else if (rnd > 0.3 && rnd <= 0.6)
                    currentNote = "6E";
                else if (rnd > 0.6 && rnd <= 0.65)
                    currentNote = "6F";
                else if (rnd > 0.65 && rnd <= 0.8)
                    currentNote = "6G";
                else if (rnd > 0.8 && rnd <= 0.95)
                    currentNote = "6A";
                else if (rnd > 0.95)
                    currentNote = "6B";
                break;
            case "6E":
                if (rnd <= 0.5)
                    currentNote = "6C";
                else if (rnd > 0.5 && rnd <= 0.6)
                    currentNote = "6D";
                else if (rnd > 0.6 && rnd <= 0.7)
                    currentNote = "6E";
                else if (rnd > 0.7 && rnd <= 0.9)
                    currentNote = "6F";
                else if (rnd > 0.9 && rnd <= 0.95)
                    currentNote = "6G";
                else if (rnd > 0.95 && rnd <= 0.975)
                    currentNote = "6A";
                else if (rnd > 0.975)
                    currentNote = "6B";
                break;
            case "6F":
                if (rnd <= 0.8)
                    currentNote = "6C";
                else if (rnd > 0.8 && rnd <= 0.85)
                    currentNote = "6D";
                else if (rnd > 0.85 && rnd <= 0.9)
                    currentNote = "6E";
                else if (rnd > 0.9 && rnd <= 0.925)
                    currentNote = "6F";
                else if (rnd > 0.925 && rnd <= 0.95)
                    currentNote = "6G";
                else if (rnd > 0.95 && rnd <= 0.975)
                    currentNote = "6A";
                else if (rnd > 0.975)
                    currentNote = "6B";
                break;
            case "6G":
                if (rnd <= 0.3)
                    currentNote = "6C";
                else if (rnd > 0.3 && rnd <= 0.45)
                    currentNote = "6D";
                else if (rnd > 0.45 && rnd <= 0.55)
                    currentNote = "6E";
                else if (rnd > 0.55 && rnd <= 0.70)
                    currentNote = "6F";
                else if (rnd > 0.70 && rnd <= 0.80)
                    currentNote = "6G";
                else if (rnd > 0.80 && rnd <= 0.90)
                    currentNote = "6A";
                else if (rnd > 0.90)
                    currentNote = "6B";
                break;
            case "6A":
                if (rnd <= 0.1)
                    currentNote = "6C";
                else if (rnd > 0.1 && rnd <= 0.15)
                    currentNote = "6D";
                else if (rnd > 0.15 && rnd <= 0.25)
                    currentNote = "6E";
                else if (rnd > 0.25 && rnd <= 0.4)
                    currentNote = "6F";
                else if (rnd > 0.4 && rnd <= 0.5)
                    currentNote = "6G";
                else if (rnd > 0.5 && rnd <= 0.75)
                    currentNote = "6A";
                else if (rnd > 0.75)
                    currentNote = "6B";
                break;
            case "6B":
                if (rnd <= 0.853)
                    currentNote = "6C";
                else if (rnd > 0.853 && rnd <= 0.877)
                    currentNote = "6D";
                else if (rnd > 0.877 && rnd <= 0.901)
                    currentNote = "6E";
                else if (rnd > 0.901 && rnd <= 0.925)
                    currentNote = "6F";
                else if (rnd > 0.925 && rnd <= 0.949)
                    currentNote = "6G";
                else if (rnd > 0.949 && rnd <= 0.973)
                    currentNote = "6A";
                else if (rnd > 0.973)
                    currentNote = "6B";
                break;
        }
        return currentNote;
    }
}